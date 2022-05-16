package com.psinder.account.commands

import com.psinder.account.AccountMongoRepository
import com.psinder.account.AccountRepository
import com.psinder.account.AccountStatus
import com.psinder.account.LogInFailureError
import com.psinder.account.accountAggregateType
import com.psinder.account.accountModule
import com.psinder.account.createAccount
import com.psinder.account.events.AccountLoggedInFailureEvent
import com.psinder.account.events.AccountLoggedInSuccessEvent
import com.psinder.account.requests.LoginAccountRequest
import com.psinder.config.JwtAuthConfig
import com.psinder.database.DatabaseTest
import com.psinder.database.RecordingEventStoreDB
import com.psinder.events.getAs
import com.psinder.kediatr.kediatrTestModule
import com.psinder.shared.password.RawPassword
import com.psinder.test.utils.faker
import com.trendyol.kediatr.CommandBus
import io.kotest.core.test.TestCase
import io.traxter.eventstoredb.EventStoreDB
import io.traxter.eventstoredb.StreamName
import org.koin.core.component.get
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.brightinventions.codified.enums.codifiedEnum
import strikt.api.expectThat
import strikt.arrow.isRight
import strikt.assertions.hasSize
import strikt.assertions.isA
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo

private val testingModules = module {
    single { RecordingEventStoreDB() } bind EventStoreDB::class
    single { AccountMongoRepository(get<CoroutineDatabase>().getCollection()) } bind AccountRepository::class
    single { LoginAccountHandler(JwtAuthConfig, get(), get()) }
}.plus(kediatrTestModule)

class LoginAccountHandlerTest : DatabaseTest(testingModules) {
    private val commandBus = get<CommandBus>()
    private val eventStore = get<RecordingEventStoreDB>()

    override fun beforeEach(testCase: TestCase) {
        eventStore.clean()
        super.beforeEach(testCase)
    }

    init {
        describe("LoginAccountHandler") {

            it("should return a token") {
                // arrange
                val rawPassword = RawPassword("SuperSecretPassword")
                val account =
                    createAccount(password = rawPassword.hashpw(), status = AccountStatus.Active.codifiedEnum())
                val email = account.email

                // act
                val command = LoginAccountCommand(LoginAccountRequest(email, rawPassword))

                // assert
                val result = commandBus.executeCommandAsync(command)

                // assert
                val events = eventStore.readStream(StreamName("${accountAggregateType.type}-${account.id}")).events
                expectThat(result)
                    .isA<LoginAccountCommandSucceed>()
                expectThat(events)
                    .hasSize(1)
                    .get { first().originalEvent.getAs<AccountLoggedInSuccessEvent>() }
                    .isRight()
                    .get { value.accountId }.isEqualTo(account.id.cast())
            }

            it("should return error when account does not exist") {
                // arrange
                val email = faker.accountModule.emailAddress()
                val password = faker.accountModule.rawPassword()

                // act
                val command = LoginAccountCommand(LoginAccountRequest(email, password))

                // assert
                val result = commandBus.executeCommandAsync(command)

                // assert
                val events = eventStore.readAll().events
                expectThat(result)
                    .isA<LoginAccountCommandFailure>()
                    .and { get { this.email }.isEqualTo(email) }
                    .and { get { errorCode }.isEqualTo(LogInFailureError.AccountNotFound.codifiedEnum()) }
                expectThat(events)
                    .isEmpty()
            }

            it("should return error when account is not active") {
                // arrange
                val rawPassword = RawPassword("SuperSecretPassword")
                val account =
                    createAccount(password = rawPassword.hashpw(), status = AccountStatus.Staged.codifiedEnum())
                val email = account.email

                // act
                val command = LoginAccountCommand(LoginAccountRequest(email, rawPassword))

                // assert
                val result = commandBus.executeCommandAsync(command)

                // assert
                val events = eventStore.readStream(StreamName("${accountAggregateType.type}-${account.id}")).events
                expectThat(result)
                    .isA<LoginAccountCommandFailure>()
                    .and { get { this.email }.isEqualTo(email) }
                    .and { get { errorCode }.isEqualTo(LogInFailureError.AccountNotActivated.codifiedEnum()) }
                expectThat(events)
                    .hasSize(1)
                    .get { first().originalEvent.getAs<AccountLoggedInFailureEvent>() }
                    .isRight()
                    .and { get { value.accountId }.isEqualTo(account.id.cast()) }
                    .and { get { value.error }.isEqualTo(LogInFailureError.AccountNotActivated.codifiedEnum()) }
            }

            it("should return error when account is not suspended") {
                // arrange
                val rawPassword = RawPassword("SuperSecretPassword")
                val account =
                    createAccount(password = rawPassword.hashpw(), status = AccountStatus.Suspended.codifiedEnum())
                val email = account.email

                // act
                val command = LoginAccountCommand(LoginAccountRequest(email, rawPassword))

                // assert
                val result = commandBus.executeCommandAsync(command)

                // assert
                val events = eventStore.readStream(StreamName("${accountAggregateType.type}-${account.id}")).events
                expectThat(result)
                    .isA<LoginAccountCommandFailure>()
                    .and { get { this.email }.isEqualTo(email) }
                    .and { get { errorCode }.isEqualTo(LogInFailureError.AccountSuspended.codifiedEnum()) }
                expectThat(events)
                    .hasSize(1)
                    .get { first().originalEvent.getAs<AccountLoggedInFailureEvent>() }
                    .isRight()
                    .and { get { value.accountId }.isEqualTo(account.id.cast()) }
                    .and { get { value.error }.isEqualTo(LogInFailureError.AccountSuspended.codifiedEnum()) }
            }

            it("should return error when password does not match") {
                // arrange
                val rawPassword = RawPassword("SuperSecretPassword")
                val invalidPassword = RawPassword("InvalidPassword")
                val account =
                    createAccount(password = rawPassword.hashpw(), status = AccountStatus.Suspended.codifiedEnum())
                val email = account.email

                // act
                val command = LoginAccountCommand(LoginAccountRequest(email, invalidPassword))

                // assert
                val result = commandBus.executeCommandAsync(command)

                // assert
                val events = eventStore.readStream(StreamName("${accountAggregateType.type}-${account.id}")).events
                expectThat(result)
                    .isA<LoginAccountCommandFailure>()
                    .and { get { this.email }.isEqualTo(email) }
                    .and { get { errorCode }.isEqualTo(LogInFailureError.InvalidPassword.codifiedEnum()) }
                expectThat(events)
                    .hasSize(1)
                    .get { first().originalEvent.getAs<AccountLoggedInFailureEvent>() }
                    .isRight()
                    .and { get { value.accountId }.isEqualTo(account.id.cast()) }
                    .and { get { value.error }.isEqualTo(LogInFailureError.InvalidPassword.codifiedEnum()) }
            }
        }
    }
}
