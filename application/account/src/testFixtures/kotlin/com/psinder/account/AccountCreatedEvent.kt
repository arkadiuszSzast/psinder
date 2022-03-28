package com.psinder.account

import com.eventstore.dbclient.Position
import com.eventstore.dbclient.RecordedEvent
import com.eventstore.dbclient.StreamRevision
import com.psinder.database.getSystemMetadata
import com.psinder.events.aggregateType
import com.psinder.events.toEventData

fun AccountCreatedEvent.recordedEvent(): RecordedEvent {
    val eventData = this.toEventData<Account, AccountCreatedEvent>()

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
