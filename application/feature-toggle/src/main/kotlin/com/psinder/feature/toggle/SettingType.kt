package com.psinder.feature.toggle

import kotlinx.serialization.KSerializer
import pl.brightinventions.codified.Codified
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.serializer.codifiedEnumSerializer

enum class SettingType(override val code: kotlin.String) : Codified<String> {
    Boolean("boolean"),
    String("string"),
    Int("int"),
    Double("double");

    object CodifiedSerializer : KSerializer<CodifiedEnum<SettingType, kotlin.String>> by codifiedEnumSerializer()
}
