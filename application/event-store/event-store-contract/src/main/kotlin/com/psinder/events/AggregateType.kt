package com.psinder.events

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class AggregateType(val type: String)

val AggregateType.streamName
    get() = "\$ce-$type"
