package com.psinder.feature.toggle

import kotlinx.serialization.KSerializer
import pl.brightinventions.codified.Codified
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.serializer.codifiedEnumSerializer

enum class Comparator(override val code: String) : Codified<String> {
    IsOneOf("isOneOf"),
    IsNotOneOf("isNotOneOf"),
    Contains("contains"),
    DoesNotContain("doesNotContain"),
    SemVerIsOneOf("semVerIsOneOf"),
    SemVerIsNotOneOf("semVerIsNotOneOf"),
    SemVerLess("semVerLess"),
    SemVerLessOrEquals("semVerLessOrEquals"),
    SemVerGreater("semVerGreater"),
    SemVerGreaterOrEquals("semVerGreaterOrEquals"),
    NumberEquals("numberEquals"),
    NumberDoesNotEqual("numberDoesNotEqual"),
    NumberLess("numberLess"),
    NumberLessOrEquals("numberLessOrEquals"),
    NumberGreater("numberGreater"),
    NumberGreaterOrEquals("numberGreaterOrEquals"),
    SensitiveIsOneOf("sensitiveIsOneOf"),
    SensitiveIsNotOneOf("sensitiveIsNotOneOf");

    object CodifiedSerializer : KSerializer<CodifiedEnum<Comparator, String>> by codifiedEnumSerializer()
}
