package com.psinder.account.subscribers

import arrow.core.nel
import com.psinder.account.events.AccountCreatedEvent
import com.psinder.account.AccountMongoRepository
import com.psinder.account.AccountRepository
import com.psinder.account.accountModule
import com.psinder.database.DatabaseTest
import com.psinder.database.recordedEvent
import com.psinder.shared.get
import com.psinder.test.utils.faker
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.test.get
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.newId
import pl.brightinventions.codified.enums.codifiedEnum
import strikt.api.expectThat
import strikt.assertions.isEqualTo

private val testingModules = module {
    single { AccountProjectionUpdater(get()) }
    single { AccountMongoRepository(get<CoroutineDatabase>().getCollection()) } bind AccountRepository::class
}

class AccountProjectionUpdaterKtTest : DatabaseTest(testingModules.nel()) {
    private val updater = get<AccountProjectionUpdater>()
    private val accountRepository = get<AccountRepository>()

    init {
        describe("account projection updater") {

            it("create account on account-created event") {
                // arrange
                val event = AccountCreatedEvent(
                    newId(),
                    faker.accountModule.emailAddress(),
                    faker.accountModule.personalData(),
                    faker.accountModule.hashedPassword(),
                    faker.accountModule.created(),
                    faker.accountModule.accountStatus().codifiedEnum(),
                    faker.accountModule.role().codifiedEnum(),
                    faker.accountModule.timeZone()
                )
                val recordedEvent = event.recordedEvent<AccountCreatedEvent>()

                // act
                updater.update(recordedEvent)

                // assert
                val account = accountRepository.findById(event.accountId).get()
                expectThat(account) {
                    get { email }.isEqualTo(event.email)
                    get { personalData }.isEqualTo(event.personalData)
                    get { password }.isEqualTo(event.password)
                    get { status }.isEqualTo(event.status)
                    get { role }.isEqualTo(event.role)
                    get { timeZoneId }.isEqualTo(event.timeZoneId)
                }
            }
        }
    }
}
