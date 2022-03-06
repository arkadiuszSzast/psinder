package com.psinder.events

import kotlinx.serialization.KSerializer
import pl.brightinventions.codified.Codified
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.serializer.codifiedEnumSerializer

enum class EventFamily(override val code: String) : Codified<String> {
    Account("account");

    object CodifiedSerializer : KSerializer<CodifiedEnum<EventFamily, String>> by codifiedEnumSerializer()
}

val EventFamily.streamName
    get() = "\$ce-$code"
