package com.psinder.account

import com.eventstore.dbclient.ReadStreamOptions
import com.psinder.account.activation.ActivateAccountSuccessfullyResponse
import com.psinder.account.activation.requests.ActivateAccountRequest
import com.psinder.account.commands.CreateAccountCommandResult
import com.psinder.account.events.AccountLoggedInFailureEvent
import com.psinder.account.requests.CreateAccountRequest
import com.psinder.account.requests.LoginAccountRequest
import com.psinder.account.support.IntegrationTest
import com.psinder.events.getAs
import com.psinder.integrationTestModules
import com.psinder.test.utils.contentAs
import com.psinder.test.utils.faker
import com.psinder.test.utils.setBody
import com.psinder.test.utils.withTestApplicationSuspending
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.traxter.eventstoredb.StreamName
import kotlinx.coroutines.delay
import org.koin.core.component.get
import pl.brightinventions.codified.enums.codifiedEnum
import strikt.api.expectThat
import strikt.arrow.isRight
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

class RoutingKtTest : IntegrationTest(accountTestingModules) {

    private val accountRepository = get<AccountRepository>()

    init {

        describe("Account API") {

            it("should create new account") {
                withTestApplicationSuspending({
                    integrationTestModules()
                    configureAccountRouting()
                    configureEventStoreSubscribers()
                }) {
                    val personalData = faker.accountModule.personalData()
                    val email = faker.accountModule.emailAddress()
                    val password = faker.accountModule.rawPassword()
                    val timeZone = faker.accountModule.timeZone()
                    with(
                        handleRequest(HttpMethod.Post, AccountApi.v1) {
                            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                            setBody(CreateAccountRequest(personalData, email, password, timeZone))
                        }
                    ) {
                        val content = response.contentAs<CreateAccountCommandResult>()
                        expectThat(response.status()).isEqualTo(HttpStatusCode.OK)
                        delay(500)
                        val savedAccount = accountRepository.findOneByEmail(email)!!
                        expectThat(savedAccount) {
                            and { get { this.personalData }.isEqualTo(personalData) }
                            and { get { this.timeZoneId }.isEqualTo(timeZone) }
                            and { get { this.password.matches(password) }.isTrue() }
                            and { get { this.status }.isEqualTo(AccountStatus.Staged.codifiedEnum()) }
                        }
                        expectThat(savedAccount.id).isEqualTo(content.accountId.cast())
                        expectThat(mailSender.getAll()).hasSize(1)
                    }
                }
            }

            it("can activate account") {
                withTestApplicationSuspending({
                    integrationTestModules()
                    configureAccountRouting()
                    configureEventStoreSubscribers()
                }) {
                    val account = createAccount(status = AccountStatus.Staged.codifiedEnum())
                    val activationToken = createActivationToken(account.id.toString())

                    with(
                        handleRequest(HttpMethod.Post, "${AccountApi.v1}/activate") {
                            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                            setBody(ActivateAccountRequest(activationToken))
                        }
                    ) {
                        val content = response.contentAs<ActivateAccountSuccessfullyResponse>()
                        expectThat(content.accountId).isEqualTo(account.id.cast())
                        expectThat(response.status()).isEqualTo(HttpStatusCode.OK)
                        delay(300)
                        val activatedAccount = accountRepository.findOneByEmail(account.email)!!
                        expectThat(activatedAccount) {
                            and { get { this.status }.isEqualTo(AccountStatus.Active.codifiedEnum()) }
                        }
                    }
                }
            }

            it("cannot log in with not activated account") {
                withTestApplicationSuspending({
                    integrationTestModules()
                    configureAccountRouting()
                    configureEventStoreSubscribers()
                }) {
                    val password = faker.accountModule.rawPassword()
                    val account =
                        createAccount(password = password.hashpw(), status = AccountStatus.Staged.codifiedEnum())
                    with(
                        handleRequest(HttpMethod.Post, "${AccountApi.v1}/login") {
                            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                            setBody(LoginAccountRequest(account.email, password))
                        }
                    ) {
                        expectThat(response.status()).isEqualTo(HttpStatusCode.Unauthorized)
                        val accountEvents = eventStoreDb.readStream(
                            StreamName("account-${account.id}"),
                            ReadStreamOptions.get().resolveLinkTos()
                        ).events.map { it.event }
                        expectThat(accountEvents) {
                            hasSize(1)
                            get { first().getAs<AccountLoggedInFailureEvent>() }
                                .isRight()
                                .get { value }
                                .get { error }.isEqualTo(LogInFailureError.AccountNotActivated.codifiedEnum())
                        }
                    }
                }
            }
        }
    }
}
