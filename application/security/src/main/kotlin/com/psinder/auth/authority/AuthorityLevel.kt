package com.psinder.auth.authority

import kotlinx.serialization.KSerializer
import pl.brightinventions.codified.Codified
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.serializer.codifiedEnumSerializer

enum class AuthorityLevel(override val code: String) : Codified<String> {
    View("View"),
    Create("Create"),
    Update("Update");

    object CodifiedSerializer : KSerializer<CodifiedEnum<AuthorityLevel, String>> by codifiedEnumSerializer()
}
