package com.psinder.events

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class EventName(val name: String)
