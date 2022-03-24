package com.psinder.events

data class FullEventType(
    private val aggregateType: AggregateType,
    private val eventName: EventName
) {
    fun get() = "${aggregateType.type}-${eventName.name}"
}
