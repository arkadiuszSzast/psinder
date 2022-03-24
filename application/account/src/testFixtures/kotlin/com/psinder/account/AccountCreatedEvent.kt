package com.psinder.account

import com.eventstore.dbclient.Position
import com.eventstore.dbclient.RecordedEvent
import com.eventstore.dbclient.StreamRevision
import com.psinder.events.aggregateType
import com.psinder.events.toEventData
import io.ktor.http.ContentType
import kotlinx.datetime.Clock
import java.util.concurrent.TimeUnit

fun AccountCreatedEvent.recordedEvent(): RecordedEvent {
    val eventData = this.toEventData<Account, AccountCreatedEvent>()
    val currentMillis = Clock.System.now().toEpochMilliseconds()
    val created = (TimeUnit.MICROSECONDS.convert(currentMillis, TimeUnit.MILLISECONDS) * 10).toString()
    val isJson = eventData.contentType == ContentType.Application.Json.toString()
    val systemMetadata = mapOf(
        "content-type" to eventData.contentType,
        "type" to eventData.eventType,
        "created" to created,
        "is-json" to isJson.toString()
    )

    return RecordedEvent(
        "$aggregateType-$aggregateId",
        StreamRevision(0),
        eventData.eventId,
        Position.START,
        systemMetadata,
        eventData.eventData,
        eventData.userMetadata
    )
}
