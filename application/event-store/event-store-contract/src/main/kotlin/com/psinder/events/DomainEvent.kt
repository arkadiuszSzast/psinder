package com.psinder.events

import com.trendyol.kediatr.Notification
import org.litote.kmongo.Id
import java.util.UUID

interface DomainEvent : Notification {

    val eventId: UUID

    val aggregateId: Id<*>

    val eventName: EventName

    val aggregateType: AggregateType
}

inline val DomainEvent.streamName: String
    get() = "${aggregateType.type}-$aggregateId"

inline val DomainEvent.fullEventType: FullEventType
    get() = FullEventType(aggregateType, eventName)
