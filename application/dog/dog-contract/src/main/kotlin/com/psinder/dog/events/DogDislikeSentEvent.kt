package com.psinder.dog.events

import com.psinder.dog.DogProfileDto
import com.psinder.dog.dogAggregateType
import com.psinder.events.AggregateType
import com.psinder.events.DomainEvent
import com.psinder.events.EventName
import com.psinder.events.FullEventType
import com.psinder.shared.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import java.util.UUID

@Serializable
data class DogDislikeSentEvent(
    @Contextual val dogId: Id<DogProfileDto>,
    @Contextual val targetDogId: Id<DogProfileDto>
) : DomainEvent {
    @Serializable(with = UUIDSerializer::class)
    override val eventId: UUID = UUID.randomUUID()

    @Contextual
    override val aggregateId = dogId

    override val eventName = DogLikeSentEvent.eventName

    override val aggregateType: AggregateType = dogAggregateType

    companion object {
        val eventName: EventName = EventName("dislike-sent")
        val fullEventType = FullEventType(dogAggregateType, eventName)
    }
}
