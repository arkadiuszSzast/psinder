package com.psinder.dog.subscribers

import com.psinder.dog.dogAggregateType
import com.psinder.events.streamName
import io.ktor.application.Application
import io.traxter.eventstoredb.EventStoreDB
import io.traxter.eventstoredb.StreamGroup
import io.traxter.eventstoredb.StreamName
import kotlinx.coroutines.launch

internal fun Application.dogProfileProjectionUpdater(
    eventStoreDb: EventStoreDB,
    dogProjectionUpdater: DogProfileProjectionUpdater
) = launch {
    eventStoreDb.subscribeToPersistedStream(
        StreamName(dogAggregateType.streamName),
        StreamGroup("dog-profile-projection-updater")
    ) { _, event ->
        dogProjectionUpdater.update(event.event)
    }
}
