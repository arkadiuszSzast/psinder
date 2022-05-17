package com.psinder.dog.subscribers

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import com.eventstore.dbclient.RecordedEvent
import com.psinder.dog.DogOverviewProjection
import com.psinder.dog.DogOverviewRepository
import com.psinder.dog.events.DogRegisteredEvent
import com.psinder.events.getAs
import mu.KotlinLogging

internal class DogOverviewProjectionUpdater(private val dogRepository: DogOverviewRepository) {
    private val log = KotlinLogging.logger {}

    suspend fun update(event: RecordedEvent) {
        when (event.eventType) {
            DogRegisteredEvent.fullEventType.get() -> applyDogRegisteredEvent(event.getAs())
        }
    }

    private suspend fun applyDogRegisteredEvent(event: Either<Throwable, DogRegisteredEvent>) {
        event.map { dogRepository.save(DogOverviewProjection.applyRegisteredEvent(it)) }
            .tap { log.debug("Stream group: dog-overview-projection-updater applied dog-registered event for aggregate ${it?.upsertedId}") }
            .bind()
    }
}
