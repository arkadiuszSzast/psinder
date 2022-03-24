package com.psinder.account.subscribers

import com.psinder.account.accountAggregateType
import com.psinder.events.streamName
import io.ktor.application.Application
import io.traxter.eventstoredb.EventStoreDB
import io.traxter.eventstoredb.StreamGroup
import io.traxter.eventstoredb.StreamName
import kotlinx.coroutines.launch

internal fun Application.accountProjectionUpdater(
    eventStoreDb: EventStoreDB,
    accountProjectionUpdater: AccountProjectionUpdater
) =
    launch {
        eventStoreDb.subscribeToPersistedStream(
            StreamName(accountAggregateType.streamName),
            StreamGroup("account-projection-updater")
        ) { _, event ->
            accountProjectionUpdater.update(event.event)
        }
    }
