package com.psinder.shared.sse

import io.ktor.application.ApplicationCall
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.response.cacheControl
import io.ktor.response.header
import io.ktor.response.respondTextWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class SseEvent<T>(val data: T, val event: String, @Contextual val id: Id<SseEvent<T>>)

suspend fun <T> ApplicationCall.respondSse(events: ReceiveChannel<SseEvent<T>>) {
    response.cacheControl(CacheControl.NoCache(null))
    response.header("Connection", "keep-alive")

    respondTextWriter(contentType = ContentType.Text.EventStream) {
        withContext(Dispatchers.IO) {
            write("id: ${newId<SseEvent<T>>()}\n")
            write("event: subscription-started\n")
            write("data: started at ${Clock.System.now()}\n")
            flush()
        }
        for (event in events) {
            withContext(Dispatchers.IO) {
                write("id: ${event.id}\n")
                write("event: ${event.event}\n")
                write("data: ${event.data}\n")
                flush()
            }
        }
    }
}
