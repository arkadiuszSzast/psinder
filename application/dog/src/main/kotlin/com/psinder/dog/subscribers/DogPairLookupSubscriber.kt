package com.psinder.dog.subscribers

import com.psinder.dog.DogProfileRepository
import com.psinder.dog.events.DogLikeSentEvent
import com.psinder.dog.events.DogPairFoundEvent
import com.psinder.dog.pairs.DogPair
import com.psinder.dog.vote.likes
import com.psinder.events.getAs
import com.psinder.events.getMetadata
import com.psinder.events.streamName
import com.psinder.events.toCommandMetadata
import com.psinder.events.toEventData
import io.ktor.application.Application
import io.traxter.eventstoredb.EventStoreDB
import io.traxter.eventstoredb.StreamGroup
import io.traxter.eventstoredb.StreamName
import kotlinx.coroutines.launch

internal fun Application.dogPairLookupSubscriber(
    eventStoreDb: EventStoreDB,
    dogProfileRepository: DogProfileRepository
) = launch {
    eventStoreDb.subscribeToPersistedStream(
        StreamName(DogLikeSentEvent.fullEventType.streamName()),
        StreamGroup("dog-pair-lookup"),
    ) { _, event ->
        val dogLikeSentEvent = event.event
        val eventMetadata = dogLikeSentEvent.getMetadata().orNull()

        dogLikeSentEvent.getAs<DogLikeSentEvent>()
            .map { event ->
                val targetDogId = event.targetDogId

                dogProfileRepository.findById(targetDogId.cast())
                    .tap {
                        val isPair = it.votes.likes().any { it.targetDogId == event.dogId }
                        if (isPair) {
                            val sourceDogPairFoundEvent = DogPairFoundEvent(event.dogId, targetDogId)
                            val targetDogPairFoundEvent = DogPairFoundEvent(targetDogId, event.dogId)
                            eventStoreDb.appendToStream(
                                sourceDogPairFoundEvent.streamName,
                                sourceDogPairFoundEvent.toEventData(eventMetadata?.toCommandMetadata()),
                            )
                            eventStoreDb.appendToStream(
                                targetDogPairFoundEvent.streamName,
                                targetDogPairFoundEvent.toEventData(eventMetadata?.toCommandMetadata()),
                            )
                        }
                    }
            }

    }
}