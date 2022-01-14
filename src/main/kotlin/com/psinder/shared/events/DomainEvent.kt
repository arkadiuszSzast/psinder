package com.psinder.shared.events

import an.awesome.pipelinr.Notification
import org.litote.kmongo.Id
import org.litote.kmongo.id.UUIDStringIdGenerator
import java.util.UUID

internal interface DomainEvent : Notification {
    val eventId: Id<DomainEvent>
        get() = UUIDStringIdGenerator.generateNewId()

    val eventType: EventType

    val eventFamily: EventFamily
}

internal val Id<DomainEvent>.uuid: UUID
    get() = UUID.fromString(this.toString())
