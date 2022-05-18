package com.psinder.dog.vote

import kotlinx.serialization.KSerializer
import pl.brightinventions.codified.Codified
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.serializer.codifiedEnumSerializer

enum class VoteOption(override val code: String) : Codified<String> {
    Like("like"),
    Dislike("dislike");

    object CodifiedSerializer : KSerializer<CodifiedEnum<VoteOption, String>> by codifiedEnumSerializer()
}
