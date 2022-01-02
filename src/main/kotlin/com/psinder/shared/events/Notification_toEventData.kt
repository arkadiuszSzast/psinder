package com.psinder.shared.events

import com.eventstore.dbclient.EventData
import com.psinder.jackson.JsonMapper

internal fun DomainEvent.toEventData(): EventData {
    val objectMapper = JsonMapper.defaultMapper
    val valueAsBytes = objectMapper.writeValueAsBytes(this)
    return EventData(this.eventId.uuid, this.eventType.code, "application/json", valueAsBytes, null)
}
