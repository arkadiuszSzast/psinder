package com.psinder.database

import com.eventstore.dbclient.EventData
import io.ktor.http.ContentType
import kotlinx.datetime.Clock
import java.util.concurrent.TimeUnit

fun EventData.getSystemMetadata(): Map<String, String> {
    val currentMillis = Clock.System.now().toEpochMilliseconds()
    val created = (TimeUnit.MICROSECONDS.convert(currentMillis, TimeUnit.MILLISECONDS) * 10).toString()
    val isJson = contentType == ContentType.Application.Json.toString()
    return mapOf(
        "content-type" to contentType,
        "type" to eventType,
        "created" to created,
        "is-json" to isJson.toString()
    )
}
