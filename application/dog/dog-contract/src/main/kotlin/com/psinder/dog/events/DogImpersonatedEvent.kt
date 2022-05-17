package com.psinder.dog.events

import com.psinder.auth.account.AccountId
import com.psinder.dog.DogProfileDto
import com.psinder.dog.ImpersonatingError
import com.psinder.dog.dogAggregateType
import com.psinder.events.DomainEvent
import com.psinder.events.EventName
import com.psinder.events.FullEventType
import com.psinder.shared.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import pl.brightinventions.codified.enums.CodifiedEnum
import java.util.UUID

@Serializable
sealed class DogImpersonatedEvent : DomainEvent

@Serializable
data class DogImpersonatedSuccessfullyEvent(
    @Contextual val dogId: Id<DogProfileDto>,
    val impersonatorId: AccountId
) : DogImpersonatedEvent() {
    @Serializable(with = UUIDSerializer::class)
    override val eventId: UUID = UUID.randomUUID()

    @Contextual
    override val aggregateId = dogId

    override val eventName = Companion.eventName

    override val aggregateType = dogAggregateType

    companion object {
        val eventName: EventName = EventName("impersonated-successfully")
        val fullEventType = FullEventType(dogAggregateType, eventName)
    }
}

@Serializable
data class DogImpersonatingFailedEvent(
    @Contextual val dogId: Id<DogProfileDto>,
    @Serializable(with = ImpersonatingError.CodifiedSerializer::class)
    val error: CodifiedEnum<ImpersonatingError, String>
) : DogImpersonatedEvent() {
    @Serializable(with = UUIDSerializer::class)
    override val eventId: UUID = UUID.randomUUID()

    @Contextual
    override val aggregateId = dogId

    override val eventName = Companion.eventName

    override val aggregateType = dogAggregateType

    companion object {
        val eventName: EventName = EventName("impersonating-failed")
        val fullEventType = FullEventType(dogAggregateType, eventName)
    }
}
