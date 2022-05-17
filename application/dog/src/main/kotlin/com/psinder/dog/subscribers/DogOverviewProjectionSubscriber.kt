package com.psinder.dog.subscribers

import com.psinder.dog.dogAggregateType
import com.psinder.events.streamName
import io.ktor.application.Application
import io.traxter.eventstoredb.EventStoreDB
import io.traxter.eventstoredb.StreamGroup
import io.traxter.eventstoredb.StreamName
import kotlinx.coroutines.launch

internal fun Application.dogOverviewProjectionUpdater(
    eventStoreDb: EventStoreDB,
    dogProjectionUpdater: DogOverviewProjectionUpdater
) = launch {
    eventStoreDb.subscribeToPersistedStream(
        StreamName(dogAggregateType.streamName),
        StreamGroup("dog-overview-projection-updater")
    ) { _, event ->
        dogProjectionUpdater.update(event.event)
    }
}
