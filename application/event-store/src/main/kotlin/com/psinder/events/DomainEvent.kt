package com.psinder.events

import com.trendyol.kediatr.Notification
import org.litote.kmongo.Id
import org.litote.kmongo.id.UUIDStringIdGenerator
import pl.brightinventions.codified.enums.CodifiedEnum
import java.util.UUID

interface DomainEvent<T> : Notification {

    val eventId: Id<DomainEvent<T>>
        get() = UUIDStringIdGenerator.generateNewId()

    val aggregateId: Id<T>

    val eventType: CodifiedEnum<EventType, String>

    val eventFamily: CodifiedEnum<EventFamily, String>
}

val <T> Id<DomainEvent<T>>.uuid: UUID
    get() = UUID.fromString(this.toString())

val <T> DomainEvent<T>.streamName: String
    get() = "${eventFamily.code()}-$aggregateId"

val <T> DomainEvent<T>.fullEventType: FullEventType
    get() = FullEventType(eventFamily, eventType)
