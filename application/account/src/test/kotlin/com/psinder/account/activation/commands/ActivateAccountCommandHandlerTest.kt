package com.psinder.account.activation.commands

import com.psinder.account.AccountDto
import com.psinder.account.AccountMongoRepository
import com.psinder.account.AccountRepository
import com.psinder.account.AccountStatus
import com.psinder.account.accountAggregateType
import com.psinder.account.activation.events.AccountActivatedEvent
import com.psinder.account.activation.events.AccountActivationFailureEvent
import com.psinder.account.config.JwtConfig
import com.psinder.account.createAccount
import com.psinder.account.queries.FindAccountByIdQueryHandler
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.auth.principal.CanDoAnythingAbilityProvider
import com.psinder.auth.principal.DenyAllAbilityProvider
import com.psinder.database.DatabaseTest
import com.psinder.database.RecordingEventStoreDB
import com.psinder.events.getAs
import com.psinder.kediatr.kediatrTestModule
import com.psinder.shared.jwt.TokenMissingSubjectException
import com.psinder.test.utils.jwtToken
import io.kotest.core.test.TestCase
import io.traxter.eventstoredb.EventStoreDB
import io.traxter.eventstoredb.StreamName
import org.koin.core.component.get
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.newId
import pl.brightinventions.codified.enums.codifiedEnum
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.arrow.isRight
import strikt.assertions.hasSize
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import java.time.Instant

private val testingModules = module {
    single(named("allowAll")) { CanDoAnythingAbilityProvider() } bind AuthorizedAccountAbilityProvider::class
    single(named("denyAll")) { DenyAllAbilityProvider() } bind AuthorizedAccountAbilityProvider::class
    single(named("allowAll")) { ActivateAccountCommandHandler(JwtConfig, get(), get(), get(named("allowAll"))) }
    single(named("denyAll")) { ActivateAccountCommandHandler(JwtConfig, get(), get(), get(named("denyAll"))) }
    single { AccountMongoRepository(get<CoroutineDatabase>().getCollection()) } bind AccountRepository::class
    single { FindAccountByIdQueryHandler(get(), get(named("allowAll"))) }
    single { RecordingEventStoreDB() } bind EventStoreDB::class
}.plus(kediatrTestModule)

class ActivateAccountCommandHandlerTest : DatabaseTest(testingModules) {
    private val allowingHandler = get<ActivateAccountCommandHandler>(named("allowAll"))
    private val denyingHandler = get<ActivateAccountCommandHandler>(named("denyAll"))
    private val eventStore = get<RecordingEventStoreDB>()

    override fun beforeEach(testCase: TestCase) {
        eventStore.clean()
        super.beforeEach(testCase)
    }

    init {

        describe("ActivateAccountCommandHandler") {

            it("throw exception when no subject claim") {
                // arrange
                val token = jwtToken {
                    secret = JwtConfig.activateAccount.secret.value
                }
                val command = ActivateAccountCommand(token)

                // act && assert
                expectThrows<TokenMissingSubjectException> {
                    allowingHandler.handleAsync(command)
                }
            }

            it("save activation failed event on lack of permissions") {
                // arrange
                val accountId = newId<AccountDto>()
                val token = jwtToken {
                    subject = accountId.toString()
                    secret = JwtConfig.activateAccount.secret.value
                }
                val command = ActivateAccountCommand(token)

                // act
                val result = denyingHandler.handleAsync(command)

                // assert
                val events = eventStore.readStream(StreamName("${accountAggregateType.type}-$accountId")).events
                expectThat(result)
                    .isA<ActivateAccountCommandFailure>()
                    .get { errorCode }.isEqualTo(AccountActivationError.MissingPermissions.codifiedEnum())

                expectThat(events)
                    .hasSize(1)
                    .get { first().originalEvent.getAs<AccountActivationFailureEvent>() }
                    .isRight()
                    .get { value }
                    .and { get { this.accountId }.isEqualTo(accountId) }
                    .and { get { this.error }.isEqualTo(AccountActivationError.MissingPermissions.codifiedEnum()) }
            }

            it("save activation failed event when token signature is invalid") {
                // arrange
                val accountId = newId<AccountDto>()
                val token = jwtToken { subject = accountId.toString(); secret = "invalid" }
                val command = ActivateAccountCommand(token)

                // act
                val result = allowingHandler.handleAsync(command)

                // assert
                val events = eventStore.readStream(StreamName("${accountAggregateType.type}-$accountId")).events
                expectThat(result)
                    .isA<ActivateAccountCommandFailure>()
                    .get { errorCode }.isEqualTo(AccountActivationError.TokenInvalid.codifiedEnum())

                expectThat(events)
                    .hasSize(1)
                    .get { first().originalEvent.getAs<AccountActivationFailureEvent>() }
                    .isRight()
                    .get { value }
                    .and { get { this.accountId }.isEqualTo(accountId) }
                    .and { get { this.error }.isEqualTo(AccountActivationError.TokenInvalid.codifiedEnum()) }
            }

            it("save activation failed event when token expired") {
                // arrange
                val accountId = newId<AccountDto>()
                val token = jwtToken {
                    subject = accountId.toString()
                    expirationDate = Instant.now().minusSeconds(10)
                    secret = JwtConfig.activateAccount.secret.value
                }
                val command = ActivateAccountCommand(token)

                // act
                val result = allowingHandler.handleAsync(command)

                // assert
                val events = eventStore.readStream(StreamName("${accountAggregateType.type}-$accountId")).events
                expectThat(result)
                    .isA<ActivateAccountCommandFailure>()
                    .get { errorCode }.isEqualTo(AccountActivationError.TokenExpired.codifiedEnum())

                expectThat(events)
                    .hasSize(1)
                    .get { first().originalEvent.getAs<AccountActivationFailureEvent>() }
                    .isRight()
                    .get { value }
                    .and { get { this.accountId }.isEqualTo(accountId) }
                    .and { get { this.error }.isEqualTo(AccountActivationError.TokenExpired.codifiedEnum()) }
            }

            it("save activation failed event when account not found") {
                // arrange
                val accountId = newId<AccountDto>()
                val token = jwtToken {
                    subject = accountId.toString()
                    expirationDate = Instant.now().plusSeconds(10)
                    secret = JwtConfig.activateAccount.secret.value
                }
                val command = ActivateAccountCommand(token)

                // act
                val result = allowingHandler.handleAsync(command)

                // assert
                val events = eventStore.readStream(StreamName("${accountAggregateType.type}-$accountId")).events
                expectThat(result)
                    .isA<ActivateAccountCommandFailure>()
                    .get { errorCode }.isEqualTo(AccountActivationError.AccountNotFound.codifiedEnum())

                expectThat(events)
                    .hasSize(1)
                    .get { first().originalEvent.getAs<AccountActivationFailureEvent>() }
                    .isRight()
                    .get { value }
                    .and { get { this.accountId }.isEqualTo(accountId) }
                    .and { get { this.error }.isEqualTo(AccountActivationError.AccountNotFound.codifiedEnum()) }
            }

            it("save activation failed event when account is in suspended status") {
                // arrange
                val account = createAccount(status = AccountStatus.Suspended.codifiedEnum())
                val accountId = account.id
                val token = jwtToken {
                    subject = accountId.toString()
                    expirationDate = Instant.now().plusSeconds(10)
                    secret = JwtConfig.activateAccount.secret.value
                }
                val command = ActivateAccountCommand(token)

                // act
                val result = allowingHandler.handleAsync(command)

                // assert
                val events = eventStore.readStream(StreamName("${accountAggregateType.type}-$accountId")).events
                expectThat(result)
                    .isA<ActivateAccountCommandFailure>()
                    .get { errorCode }.isEqualTo(AccountActivationError.AccountSuspended.codifiedEnum())

                expectThat(events)
                    .hasSize(1)
                    .get { first().originalEvent.getAs<AccountActivationFailureEvent>() }
                    .isRight()
                    .get { value }
                    .and { get { this.error }.isEqualTo(AccountActivationError.AccountSuspended.codifiedEnum()) }
            }

            it("activate account") {
                // arrange
                val account = createAccount(status = AccountStatus.Staged.codifiedEnum())
                val accountId = account.id
                val token = jwtToken {
                    subject = accountId.toString()
                    expirationDate = Instant.now().plusSeconds(10)
                    secret = JwtConfig.activateAccount.secret.value
                }
                val command = ActivateAccountCommand(token)

                // act
                val result = allowingHandler.handleAsync(command)

                // assert
                val events = eventStore.readStream(StreamName("${accountAggregateType.type}-$accountId")).events
                expectThat(result)
                    .isA<ActivateAccountCommandSucceed>()
                    .get { accountId }.isEqualTo(accountId)

                expectThat(events)
                    .hasSize(1)
                    .get { first().originalEvent.getAs<AccountActivatedEvent>() }
                    .isRight()
                    .get { value }
                    .and { get { this.accountId }.isEqualTo(accountId.cast()) }
            }
        }
    }
}
