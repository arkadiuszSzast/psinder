package com.psinder.shared.events

import arrow.core.Either
import com.eventstore.dbclient.RecordedEvent
import com.psinder.shared.json.JsonMapper
import com.psinder.shared.json.decodeFromStream

internal inline fun <reified A> RecordedEvent.getAs(): Either<Throwable, A> {
    val json = JsonMapper.defaultMapper
    return json.decodeFromStream(this.eventData)
}
