package com.psinder.dog.events

import com.psinder.auth.account.AccountId
import com.psinder.dog.DogDescription
import com.psinder.dog.DogName
import com.psinder.dog.DogProfileDto
import com.psinder.dog.DogProfileImage
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
data class DogRegisteredEvent(
    @Contextual val dogId: Id<DogProfileDto>,
    val accountId: AccountId,
    val dogName: DogName,
    val dogDescription: DogDescription,
    val images: List<DogProfileImage>
) : DomainEvent {

    @Serializable(with = UUIDSerializer::class)
    override val eventId: UUID = UUID.randomUUID()

    @Contextual
    override val aggregateId = dogId

    override val eventName = Companion.eventName

    override val aggregateType: AggregateType = dogAggregateType

    companion object {
        val eventName: EventName = EventName("registered")
        val fullEventType = FullEventType(dogAggregateType, eventName)
    }
}
