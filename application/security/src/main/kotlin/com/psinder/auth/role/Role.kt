package com.psinder.auth.role

import kotlinx.serialization.KSerializer
import pl.brightinventions.codified.Codified
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.serializer.codifiedEnumSerializer

enum class Role(override val code: String) : Codified<String> {
    Admin("Admin"),
    User("User");

    object CodifiedSerializer : KSerializer<CodifiedEnum<Role, String>> by codifiedEnumSerializer()
}
