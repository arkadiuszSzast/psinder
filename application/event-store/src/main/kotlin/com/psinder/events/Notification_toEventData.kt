package com.psinder.events

import com.eventstore.dbclient.EventData
import com.eventstore.dbclient.RecordedEvent
import com.psinder.shared.json.JsonMapper
import com.trendyol.kediatr.CommandMetadata
import io.ktor.http.ContentType
import kotlinx.serialization.encodeToString
import mu.KotlinLogging

inline fun <reified T : DomainEvent> T.toEventData(
    correlationId: CorrelationId? = null,
    causationId: CausationId? = null
): EventData {
    val objectMapper = JsonMapper.defaultMapper
    val correlation = correlationId ?: CorrelationId(eventId)
    val causation = causationId ?: CausationId(eventId)
    val eventMetadata = EventMetadata(correlation, causation)

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

inline fun <reified T : DomainEvent> T.toEventData(
    parentEvent: RecordedEvent?
): EventData {
    val log = KotlinLogging.logger {}

    val parentMetadata = parentEvent?.getMetadata()?.tapLeft {
        log.warn { "Error when getting parent event metadata: ${it.stackTraceToString()}" }
    }?.orNull()
    val correlationId = parentMetadata?.correlationId ?: CorrelationId(eventId)
    val causationId = parentMetadata?.causationId ?: CausationId(eventId)

    return toEventData<T>(correlationId, causationId)
}

inline fun <reified T : DomainEvent> T.toEventData(
    commandMetadata: CommandMetadata?
): EventData {
    val correlationId = commandMetadata?.correlationId?.let { CorrelationId(it) } ?: CorrelationId(eventId)
    val causationId = commandMetadata?.causationId?.let { CausationId(it) } ?: CausationId(eventId)

    return toEventData<T>(correlationId, causationId)
}
