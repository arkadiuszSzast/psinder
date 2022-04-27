package com.psinder.feature.toggle

import kotlinx.serialization.KSerializer
import pl.brightinventions.codified.Codified
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.serializer.codifiedEnumSerializer

enum class ComparisonAttribute(override val code: String) : Codified<String> {
    Identifier("Identifier"),
    Email("Email"),
    Country("Country");

    object CodifiedSerializer : KSerializer<CodifiedEnum<ComparisonAttribute, String>> by codifiedEnumSerializer()
}
