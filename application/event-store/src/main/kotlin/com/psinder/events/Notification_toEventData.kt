package com.psinder.events

import com.eventstore.dbclient.EventData
import com.psinder.shared.koin.getKoinInstance
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T : DomainEvent> DomainEvent.toEventData(): EventData {
    val objectMapper = getKoinInstance<Json>()
    val valueAsBytes = objectMapper.encodeToString(this as T).encodeToByteArray()
    return EventData(this.eventId.uuid, this.eventType.code, "application/json", valueAsBytes, null)
}
