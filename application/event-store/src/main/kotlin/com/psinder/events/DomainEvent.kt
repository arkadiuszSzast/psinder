package com.psinder.events

import com.trendyol.kediatr.Notification
import org.litote.kmongo.Id
import java.util.UUID

interface DomainEvent<T> : Notification {

    val eventId: UUID

    val aggregateId: Id<T>

    val eventName: EventName
}

inline val <reified T> DomainEvent<T>.streamName: String
    get() = "$aggregateType-$aggregateId"

inline val <reified T> DomainEvent<T>.fullEventType: FullEventType
    get() = FullEventType(AggregateType(aggregateType), eventName)

inline val <reified T> DomainEvent<T>.aggregateType
    get() = T::class.simpleName?.lowercase() ?: ""
