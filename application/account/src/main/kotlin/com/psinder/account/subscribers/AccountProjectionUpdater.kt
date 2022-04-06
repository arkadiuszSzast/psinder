package com.psinder.account.subscribers

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import com.eventstore.dbclient.RecordedEvent
import com.psinder.account.Account
import com.psinder.account.events.AccountCreatedEvent
import com.psinder.account.AccountRepository
import com.psinder.events.getAs
import mu.KotlinLogging

internal class AccountProjectionUpdater(private val accountRepository: AccountRepository) {
    private val log = KotlinLogging.logger {}

    suspend fun update(event: RecordedEvent) {
        when (event.eventType) {
            AccountCreatedEvent.fullEventType.get() -> applyAccountCreatedEvent(event.getAs())
        }
    }

    private suspend fun applyAccountCreatedEvent(event: Either<Throwable, AccountCreatedEvent>) {
        event.map { accountRepository.save(Account.applyCreatedEvent(it)) }
            .tap { log.info("Stream group: account-projection-updater applied account-created event for aggregate ${it?.upsertedId}") }
            .bind()
    }
}
