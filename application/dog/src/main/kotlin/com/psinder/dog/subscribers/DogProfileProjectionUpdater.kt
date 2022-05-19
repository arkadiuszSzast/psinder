package com.psinder.dog.subscribers

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import com.eventstore.dbclient.RecordedEvent
import com.psinder.dog.DogProfileProjection
import com.psinder.dog.DogProfileRepository
import com.psinder.dog.events.DogDislikeSentEvent
import com.psinder.dog.events.DogLikeSentEvent
import com.psinder.dog.events.DogPairFoundEvent
import com.psinder.dog.events.DogRegisteredEvent
import com.psinder.events.getAs
import mu.KotlinLogging

internal class DogProfileProjectionUpdater(private val dogRepository: DogProfileRepository) {
    private val log = KotlinLogging.logger {}

    suspend fun update(event: RecordedEvent) {
        when (event.eventType) {
            DogRegisteredEvent.fullEventType.get() -> applyDogRegisteredEvent(event.getAs())
            DogLikeSentEvent.fullEventType.get() -> applyDogLikedEvent(event.getAs())
            DogDislikeSentEvent.fullEventType.get() -> applyDogDislikedEvent(event.getAs())
            DogPairFoundEvent.fullEventType.get() -> applyDogPairFoundEvent(event.getAs())
        }
    }

    private suspend fun applyDogLikedEvent(event: Either<Throwable, DogLikeSentEvent>) {
        event.tapLeft { log.error("Error while applying DogLikedEvent ", it) }
            .map { event ->
                dogRepository.findById(event.dogId.cast())
                    .tapNone { "Dog wih id: ${event.dogId} not found. ${DogLikeSentEvent.fullEventType} won't be applied" }
                    .map { DogProfileProjection.applyDogLikedEvent(it, event) }
                    .map { dogRepository.updateById(it.id, it) }.tap {
                        log.debug(
                            "Stream group: dog-profile-projection-updater applied ${DogLikeSentEvent.fullEventType} event " + "for aggregate ${event.dogId}"
                        )
                    }
            }
    }

    private suspend fun applyDogDislikedEvent(event: Either<Throwable, DogDislikeSentEvent>) {
        event.tapLeft { log.error("Error while applying DogLikedEvent ", it) }
            .map { event ->
                dogRepository.findById(event.dogId.cast())
                    .tapNone { "Dog wih id: ${event.dogId} not found. ${DogDislikeSentEvent.fullEventType} won't be applied" }
                    .map { DogProfileProjection.applyDogDislikedEvent(it, event) }
                    .map { dogRepository.updateById(it.id, it) }.tap {
                        log.debug(
                            "Stream group: dog-profile-projection-updater applied ${DogDislikeSentEvent.fullEventType} event " + "for aggregate ${event.dogId}"
                        )
                    }
            }
    }

    private suspend fun applyDogPairFoundEvent(event: Either<Throwable, DogPairFoundEvent>) {
        event.tapLeft { log.error("Error while applying DogPairFoundEvent ", it) }
            .map { event ->
                dogRepository.findById(event.dogId.cast())
                    .tapNone { "Dog wih id: ${event.dogId} not found. ${DogPairFoundEvent.fullEventType} won't be applied" }
                    .map { DogProfileProjection.applyDogPairFoundEvent(it, event) }
                    .map { dogRepository.updateById(it.id, it) }.tap {
                        log.debug(
                            "Stream group: dog-profile-projection-updater applied ${DogPairFoundEvent.fullEventType} event " + "for aggregate ${event.dogId}"
                        )
                    }
            }
    }

    private suspend fun applyDogRegisteredEvent(event: Either<Throwable, DogRegisteredEvent>) {
        event.map { dogRepository.save(DogProfileProjection.applyRegisteredEvent(it)) }
            .tap { log.debug("Stream group: dog-profile-projection-updater applied ${DogRegisteredEvent.fullEventType} event for aggregate ${it?.upsertedId}") }
            .bind()
    }
}
