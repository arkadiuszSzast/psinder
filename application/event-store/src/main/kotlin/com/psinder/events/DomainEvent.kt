package com.psinder.events

import com.trendyol.kediatr.Notification
import org.litote.kmongo.Id
import org.litote.kmongo.id.UUIDStringIdGenerator
import java.util.UUID

interface DomainEvent : Notification {
    val eventId: Id<DomainEvent>
        get() = UUIDStringIdGenerator.generateNewId()

    val eventType: EventType

    val eventFamily: EventFamily
}

val Id<DomainEvent>.uuid: UUID
    get() = UUID.fromString(this.toString())
