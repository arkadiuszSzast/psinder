package com.psinder.account.subscribers

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import com.eventstore.dbclient.RecordedEvent
import com.psinder.account.AccountProjection
import com.psinder.account.AccountRepository
import com.psinder.account.activation.events.AccountActivatedEvent
import com.psinder.account.events.AccountCreatedEvent
import com.psinder.account.events.AccountLoggedInSuccessEvent
import com.psinder.events.getAs
import mu.KotlinLogging

internal class AccountProjectionUpdater(private val accountRepository: AccountRepository) {
    private val log = KotlinLogging.logger {}

    suspend fun update(event: RecordedEvent) {
        when (event.eventType) {
            AccountCreatedEvent.fullEventType.get() -> applyAccountCreatedEvent(event.getAs())
            AccountActivatedEvent.fullEventType.get() -> applyAccountActivatedEvent(event.getAs())
            AccountLoggedInSuccessEvent.fullEventType.get() -> applyAccountLoggedInSuccessEvent(event.getAs())
        }
    }

    private suspend fun applyAccountActivatedEvent(event: Either<Throwable, AccountActivatedEvent>) {
        event
            .map { event ->
                accountRepository.findById(event.accountId.cast())
                    .tapNone { "Account wih id: ${event.accountId} not found. AccountActivatedEvent won't be applied" }
                    .map { AccountProjection.applyActivatedEvent(it, event) }
                    .map { accountRepository.updateById(it.id, it) }
                    .tap {
                        log.debug(
                            "Stream group: account-projection-updater applied account-activated event " +
                                "for aggregate ${event.accountId}"
                        )
                    }
            }
            .bind()
    }

    private suspend fun applyAccountCreatedEvent(event: Either<Throwable, AccountCreatedEvent>) {
        event.map { accountRepository.save(AccountProjection.applyCreatedEvent(it)) }
            .tap { log.debug("Stream group: account-projection-updater applied account-created event for aggregate ${it?.upsertedId}") }
            .bind()
    }

    private suspend fun applyAccountLoggedInSuccessEvent(event: Either<Throwable, AccountLoggedInSuccessEvent>) {
        event
            .map { event ->
                accountRepository.findById(event.accountId.cast())
                    .tapNone { "Account wih id: ${event.accountId} not found. AccountLoggedInSuccessEvent won't be applied" }
                    .map { AccountProjection.applyLoggedInSuccessEvent(it, event) }
                    .map { accountRepository.updateById(it.id, it) }
                    .tap {
                        log.debug(
                            "Stream group: account-projection-updater applied account-logged-in-successfully " +
                                "for aggregate ${event.accountId}"
                        )
                    }
            }
            .bind()
    }
}
