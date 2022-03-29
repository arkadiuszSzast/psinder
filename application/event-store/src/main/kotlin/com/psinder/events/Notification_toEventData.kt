package com.psinder.events

import com.eventstore.dbclient.EventData
import com.psinder.shared.json.JsonMapper
import io.ktor.http.ContentType
import kotlinx.serialization.encodeToString

inline fun <reified R, reified T : DomainEvent<R>> DomainEvent<R>.toEventData(metadata: EventMetadata? = null): EventData {
    val objectMapper = JsonMapper.defaultMapper
    val eventMetadata = metadata ?: EventMetadata(CorrelationId(eventId), CausationId(eventId))
    val eventMetadataAsBytes = objectMapper.encodeToString(eventMetadata).encodeToByteArray()

    val valueAsBytes = objectMapper.encodeToString(this as T).encodeToByteArray()
    return EventData(
        this.eventId,
        this.fullEventType.get(),
        ContentType.Application.Json.toString(),
        valueAsBytes,
        eventMetadataAsBytes
    )
}
