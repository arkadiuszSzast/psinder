package com.psinder.shared.events

import arrow.core.Either
import com.eventstore.dbclient.RecordedEvent
import com.psinder.jackson.JsonMapper
import com.psinder.shared.toObject

internal inline fun <reified A> RecordedEvent.getAs(): Either<Throwable, A> {
    val objectMapper = JsonMapper.defaultMapper
    return objectMapper.toObject<A>(this.eventData)
}
