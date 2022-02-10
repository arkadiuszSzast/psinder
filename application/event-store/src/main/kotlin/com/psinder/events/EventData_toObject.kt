package com.psinder.events

import arrow.core.Either
import com.eventstore.dbclient.RecordedEvent
import com.psinder.shared.json.decodeFromStream
import com.psinder.shared.koin.getKoinInstance
import kotlinx.serialization.json.Json

inline fun <reified A> RecordedEvent.getAs(): Either<Throwable, A> {
    val json = getKoinInstance<Json>()
    return json.decodeFromStream(this.eventData)
}
