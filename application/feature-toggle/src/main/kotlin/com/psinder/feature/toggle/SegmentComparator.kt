package com.psinder.feature.toggle

import kotlinx.serialization.KSerializer
import pl.brightinventions.codified.Codified
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.serializer.codifiedEnumSerializer

enum class SegmentComparator(override val code: String) : Codified<String> {
    IsIn("isIn"),
    IsNotIn("isNotIn");

    object CodifiedSerializer : KSerializer<CodifiedEnum<SegmentComparator, String>> by codifiedEnumSerializer()
}
