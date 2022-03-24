package com.psinder.account.subscribers

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import com.psinder.account.AccountCreatedEvent
import com.psinder.account.AccountRepository
import com.psinder.account.accountAggregateType
import com.psinder.events.getAs
import com.psinder.events.streamName
import io.ktor.application.Application
import io.traxter.eventstoredb.EventStoreDB
import io.traxter.eventstoredb.StreamGroup
import io.traxter.eventstoredb.StreamName
import kotlinx.coroutines.launch
import mu.KotlinLogging

internal fun Application.accountProjectionUpdater(eventStoreDb: EventStoreDB, accountRepository: AccountRepository) =
    launch {
        eventStoreDb.subscribeToPersistedStream(
            StreamName(accountAggregateType.streamName),
            StreamGroup("account-projection-updater")
        ) { _, event ->
            when (event.event.eventType) {
                AccountCreatedEvent.fullEventType.get() -> applyAccountCreatedEvent(
                    event.event.getAs(),
                    accountRepository
                )
            }
        }
    }

private suspend fun applyAccountCreatedEvent(
    event: Either<Throwable, AccountCreatedEvent>,
    accountRepository: AccountRepository
) {
    val log = KotlinLogging.logger {}
    event.map {
        accountRepository.save(it.apply()).also { result ->
            val id = result?.upsertedId
            log.info("Stream group: account-projection-updater applied account-created event for aggregate $id")
        }
    }.bind()
}
