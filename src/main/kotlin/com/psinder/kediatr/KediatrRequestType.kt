package com.psinder.kediatr

import kotlinx.serialization.KSerializer
import pl.brightinventions.codified.Codified
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.serializer.codifiedEnumSerializer

internal enum class KediatrRequestType(override val code: String) : Codified<String> {
    Command("command"),
    CommandWithResult("command-with-result"),
    Query("query"),
    Notification("notification");

    object CodifiedSerializer : KSerializer<CodifiedEnum<KediatrRequestType, String>> by codifiedEnumSerializer()
}
