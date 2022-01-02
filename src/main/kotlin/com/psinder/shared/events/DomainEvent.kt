package com.psinder.shared.events

import an.awesome.pipelinr.Notification
import com.fasterxml.jackson.annotation.JsonIgnore
import org.litote.kmongo.Id
import org.litote.kmongo.id.UUIDStringIdGenerator
import java.util.UUID

internal interface DomainEvent : Notification {
    @get:JsonIgnore
    val eventId: Id<DomainEvent>
        get() = UUIDStringIdGenerator.generateNewId()

    @get:JsonIgnore
    val eventType: EventType

    @get:JsonIgnore
    val eventFamily: EventFamily
}

internal val Id<DomainEvent>.uuid: UUID
    get() = UUID.fromString(this.toString())
