package com.psinder.account.subscribers

import arrow.core.nel
import com.psinder.account.Account
import com.psinder.account.AccountMongoRepository
import com.psinder.account.AccountRepository
import com.psinder.account.AccountStatus
import com.psinder.account.accountAggregateType
import com.psinder.account.accountModule
import com.psinder.account.activation.subscribers.accountActivationTokensProjectionSubscriber
import com.psinder.account.events.AccountCreatedEvent
import com.psinder.account.support.DatabaseAndEventStoreTest
import com.psinder.database.EventStoreTest
import com.psinder.events.streamName
import com.psinder.events.toEventData
import com.psinder.test.utils.faker
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.ktor.server.testing.withApplication
import io.traxter.eventstoredb.StreamGroup
import io.traxter.eventstoredb.StreamName
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import org.koin.core.component.get
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.brightinventions.codified.enums.codifiedEnum
import strikt.api.expectThat
import strikt.arrow.isSome
import strikt.assertions.isEqualTo
import strikt.assertions.isNull
import java.util.concurrent.CountDownLatch

private val testingModules = module {
    single { AccountMongoRepository(get<CoroutineDatabase>().getCollection()) } bind AccountRepository::class
    single { AccountProjectionUpdater(get()) }
}

class AccountProjectionSubscriberKtTest : DatabaseAndEventStoreTest(testingModules.nel()) {
    private val accountRepository = get<AccountRepository>()

    init {

        describe("AccountProjectionSubscriber") {

            it("should create account projection on AccountCreatedEvent") {
                withApplication {
                    runBlocking {
                        //arrange
                        val accountCreatedEvent = Account.create(
                            faker.accountModule.emailAddress(),
                            faker.accountModule.personalData(),
                            faker.accountModule.role().codifiedEnum(),
                            faker.accountModule.hashedPassword(),
                            faker.accountModule.timeZone()
                        )

                        //act
                        this.application.accountProjectionUpdater(eventStoreDb, get())
                        eventStoreDb.appendToStream(
                            accountCreatedEvent.streamName,
                            accountCreatedEvent.toEventData<AccountCreatedEvent>(),
                        )
                        delay(400)

                        //assert
                        val accountProjection = accountRepository.findById(accountCreatedEvent.accountId.cast())
                        expectThat(accountProjection) {
                            isSome()
                                .get { value }
                                .and { get { email }.isEqualTo(accountCreatedEvent.email) }
                                .and { get { personalData }.isEqualTo(accountCreatedEvent.personalData) }
                                .and { get { role }.isEqualTo(accountCreatedEvent.role) }
                                .and { get { password }.isEqualTo(accountCreatedEvent.password) }
                                .and { get { timeZoneId }.isEqualTo(accountCreatedEvent.timeZoneId) }
                                .and { get { status }.isEqualTo(AccountStatus.Staged.codifiedEnum()) }
                                .and { get { created }.isEqualTo(accountCreatedEvent.created) }
                                .and { get { lastLoggedInDate }.isNull() }
                        }
                    }
                }
            }
        }
    }
}
