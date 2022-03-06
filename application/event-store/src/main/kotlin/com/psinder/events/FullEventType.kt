package com.psinder.events

import pl.brightinventions.codified.enums.CodifiedEnum

data class FullEventType(
    private val eventFamily: CodifiedEnum<EventFamily, String>,
    private val eventType: CodifiedEnum<EventType, String>
) {
    fun get() = "${eventFamily.code()}-${eventType.code()}"
}
