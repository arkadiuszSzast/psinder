package com.psinder.account.commands

import com.psinder.account.AccountCreatedEvent
import com.psinder.account.AccountMongoRepository
import com.psinder.account.AccountRepository
import com.psinder.account.AccountStatus
import com.psinder.account.accountAggregateType
import com.psinder.account.accountModule
import com.psinder.account.createAccount
import com.psinder.account.queries.FindAccountByEmailQueryHandler
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.auth.principal.CanDoAnythingAbilityProvider
import com.psinder.auth.role.Role
import com.psinder.database.DatabaseTest
import com.psinder.database.RecordingEventStoreDB
import com.psinder.events.getAs
import com.psinder.kediatr.kediatrTestModule
import com.psinder.shared.validation.ValidationException
import com.psinder.test.utils.faker
import com.psinder.test.utils.isEqualToNowIgnoringSeconds
import com.trendyol.kediatr.CommandBus
import io.kotest.core.test.TestCase
import io.kotest.matchers.nulls.shouldNotBeNull
import io.traxter.eventstoredb.EventStoreDB
import io.traxter.eventstoredb.StreamName
import org.koin.core.component.get
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.brightinventions.codified.enums.codifiedEnum
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.arrow.isRight
import strikt.assertions.hasSize
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo

private val testingModules = module {
    single { CanDoAnythingAbilityProvider() } bind AuthorizedAccountAbilityProvider::class
    single { RecordingEventStoreDB() } bind EventStoreDB::class
    single { AccountMongoRepository(get<CoroutineDatabase>().getCollection()) } bind AccountRepository::class
    single { FindAccountByEmailQueryHandler(get()) }
    single { CreateAccountHandler(get(), get(), get()) }
}.plus(kediatrTestModule)

class CreateAccountHandlerTest : DatabaseTest(testingModules) {
    private val commandBus = get<CommandBus>()
    private val eventStoreDb = get<RecordingEventStoreDB>()

    override fun beforeEach(testCase: TestCase) {
        eventStoreDb.clean()
        super.beforeEach(testCase)
    }

    init {
        describe("CreateAccountHandle") {

            it("should create account") {
                // arrange
                val request = faker.accountModule.accountRequest()

                // act
                val result = commandBus.executeCommandAsync(CreateAccountCommand(request))

                // assert
                expectThat(result.accountId).shouldNotBeNull()
                val events =
                    eventStoreDb.readStream(StreamName("${accountAggregateType.type}-${result.accountId}")).events
                expectThat(events) {
                    hasSize(1)
                    get { first().originalEvent.getAs<AccountCreatedEvent>() }
                        .isRight()
                        .get { value }
                        .and { get { accountId }.isEqualTo(result.accountId.cast()) }
                        .and { get { email }.isEqualTo(request.email) }
                        .and { get { personalData }.isEqualTo(request.personalData) }
                        .and { get { timeZoneId }.isEqualTo(request.timeZoneId) }
                        .and { get { created.value }.isEqualToNowIgnoringSeconds() }
                        .and { get { status }.isEqualTo(AccountStatus.Staged.codifiedEnum()) }
                        .and { get { role }.isEqualTo(Role.User.codifiedEnum()) }
                }
            }

            it("should not create account when email is already taken") {
                // arrange
                val request = faker.accountModule.accountRequest()
                createAccount(email = request.email)

                // act && assert
                expectThrows<ValidationException> {
                    commandBus.executeCommandAsync(CreateAccountCommand(request))
                }
                expectThat(eventStoreDb.readAll().events).isEmpty()
            }
        }
    }
}
