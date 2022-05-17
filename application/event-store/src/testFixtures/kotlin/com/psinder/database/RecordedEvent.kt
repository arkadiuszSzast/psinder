package com.psinder.database

import com.eventstore.dbclient.Position
import com.eventstore.dbclient.RecordedEvent
import com.eventstore.dbclient.StreamRevision
import com.psinder.events.DomainEvent
import com.psinder.events.toEventData

inline fun <reified T : DomainEvent> DomainEvent.recordedEvent(): RecordedEvent {
    val eventData = (this as T).toEventData()

    return RecordedEvent(
        "$aggregateType-$aggregateId",
        StreamRevision(0),
        eventData.eventId,
        Position.START,
        eventData.getSystemMetadata(),
        eventData.eventData,
        eventData.userMetadata
    )
}
