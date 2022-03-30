package com.psinder.events

import arrow.core.Either
import com.eventstore.dbclient.RecordedEvent
import com.psinder.shared.json.JsonMapper
import com.psinder.shared.json.decodeFromStream

fun RecordedEvent.getMetadata(): Either<Throwable, EventMetadata> {
    val json = JsonMapper.defaultMapper
    return json.decodeFromStream(this.userMetadata)
}
