package com.psinder.events

import com.eventstore.dbclient.EventData
import com.psinder.shared.json.JsonMapper
import io.ktor.http.ContentType
import kotlinx.serialization.encodeToString

inline fun <reified R, reified T : DomainEvent<R>> DomainEvent<R>.toEventData(): EventData {
    val objectMapper = JsonMapper.defaultMapper

    val valueAsBytes = objectMapper.encodeToString(this as T).encodeToByteArray()
    return EventData(
        this.eventId.uuid,
        this.fullEventType.get(),
        ContentType.Application.Json.toString(),
        valueAsBytes,
        null
    )
}
