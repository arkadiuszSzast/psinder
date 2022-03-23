package com.psinder.account.subscribers

import arrow.core.computations.ResultEffect.bind
import com.psinder.account.AccountCreatedEvent
import com.psinder.account.AccountRepository
import com.psinder.events.EventFamily
import com.psinder.events.getAs
import com.psinder.events.streamName
import io.ktor.application.Application
import io.ktor.application.log
import io.traxter.eventstoredb.EventStoreDB
import io.traxter.eventstoredb.StreamGroup
import kotlinx.coroutines.launch

internal fun Application.accountProjectionUpdater(eventStoreDb: EventStoreDB, accountRepository: AccountRepository) =
    launch {
        eventStoreDb.subscribeToPersistedStream(
            EventFamily.Account.streamName,
            StreamGroup("account-projection-updater")
        ) { _, event ->
            when (event.event.eventType) {
                "account-created" -> event.event.getAs<AccountCreatedEvent>()
                    .map { accountRepository.save(it.apply()) }
                    .tap { log.info("Stream group: account-projection-updater applied account-created event for aggregate ${it?.upsertedId}") }
                    .bind()
            }
        }
    }
