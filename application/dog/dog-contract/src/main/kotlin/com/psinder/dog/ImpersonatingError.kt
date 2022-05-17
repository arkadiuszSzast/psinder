package com.psinder.dog

import kotlinx.serialization.KSerializer
import pl.brightinventions.codified.Codified
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.serializer.codifiedEnumSerializer

enum class ImpersonatingError(override val code: String) : Codified<String> {
    NotFound("not_found"),
    NotAllowed("not_allowed");

    object CodifiedSerializer : KSerializer<CodifiedEnum<ImpersonatingError, String>> by codifiedEnumSerializer()
}
