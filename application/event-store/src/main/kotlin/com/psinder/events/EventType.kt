package com.psinder.events

import kotlinx.serialization.KSerializer
import pl.brightinventions.codified.Codified
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.serializer.codifiedEnumSerializer

enum class EventType(override val code: String) : Codified<String> {
    Created("created");

    object CodifiedSerializer : KSerializer<CodifiedEnum<EventType, String>> by codifiedEnumSerializer()
}
