package com.psinder.account.subscribers

import arrow.core.nel
import com.psinder.account.AccountAggregate
import com.psinder.account.AccountMongoRepository
import com.psinder.account.AccountRepository
import com.psinder.account.AccountStatus
import com.psinder.account.accountModule
import com.psinder.account.create
import com.psinder.account.events.AccountCreatedEvent
import com.psinder.account.support.DatabaseAndEventStoreTest
import com.psinder.events.streamName
import com.psinder.events.toEventData
import com.psinder.test.utils.faker
import io.ktor.server.testing.withTestApplication
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.get
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.brightinventions.codified.enums.codifiedEnum
import strikt.api.expectThat
import strikt.arrow.isSome
import strikt.assertions.isEqualTo
import strikt.assertions.isNull

private val testingModules = module {
    single { AccountMongoRepository(get<CoroutineDatabase>().getCollection()) } bind AccountRepository::class
    single { AccountProjectionUpdater(get()) }
}

class AccountProjectionSubscriberKtTest : DatabaseAndEventStoreTest(testingModules.nel()) {
    private val accountRepository = get<AccountRepository>()

    init {

        describe("AccountProjectionSubscriber") {

            it("should create account projection on AccountCreatedEvent") {
                withTestApplication {
                    val application = this.application
                    launch {
                        // arrange
                        val accountAggregateCreatedEvent = AccountAggregate.Events.create(
                            faker.accountModule.emailAddress(),
                            faker.accountModule.personalData(),
                            faker.accountModule.role().codifiedEnum(),
                            faker.accountModule.hashedPassword(),
                            faker.accountModule.timeZone()
                        )

                        // act
                        application.accountProjectionUpdater(eventStoreDb, get())
                        eventStoreDb.appendToStream(
                            accountAggregateCreatedEvent.streamName,
                            accountAggregateCreatedEvent.toEventData(),
                        )
                        delay(600)

                        // assert
                        val accountProjection =
                            accountRepository.findById(accountAggregateCreatedEvent.accountId.cast())
                        expectThat(accountProjection) {
                            isSome()
                                .get { value }
                                .and { get { email }.isEqualTo(accountAggregateCreatedEvent.email) }
                                .and { get { personalData }.isEqualTo(accountAggregateCreatedEvent.personalData) }
                                .and { get { role }.isEqualTo(accountAggregateCreatedEvent.role) }
                                .and { get { password }.isEqualTo(accountAggregateCreatedEvent.password) }
                                .and { get { timeZoneId }.isEqualTo(accountAggregateCreatedEvent.timeZoneId) }
                                .and { get { status }.isEqualTo(AccountStatus.Staged.codifiedEnum()) }
                                .and { get { created }.isEqualTo(accountAggregateCreatedEvent.created) }
                                .and { get { lastLoggedInDate }.isNull() }
                        }
                    }
                }
            }
        }
    }
}
