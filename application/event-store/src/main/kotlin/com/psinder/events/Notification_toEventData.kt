package com.psinder.events

import com.eventstore.dbclient.EventData
import com.psinder.shared.json.JsonMapper
import kotlinx.serialization.encodeToString

inline fun <reified T : DomainEvent> DomainEvent.toEventData(): EventData {
    val objectMapper = JsonMapper.defaultMapper
    val valueAsBytes = objectMapper.encodeToString(this as T).encodeToByteArray()
    return EventData(this.eventId.uuid, this.eventType.code, "application/json", valueAsBytes, null)
}
