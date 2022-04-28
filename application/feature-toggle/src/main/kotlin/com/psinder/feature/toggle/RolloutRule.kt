package com.psinder.feature.toggle

import kotlinx.serialization.Serializable
import mu.KotlinLogging
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.codifiedEnum

@Serializable
data class RolloutRule<T>(
    @Serializable(with = ComparisonAttribute.CodifiedSerializer::class)
    val comparisonAttribute: CodifiedEnum<ComparisonAttribute, String>,
    @Serializable(with = Comparator.CodifiedSerializer::class)
    val comparator: CodifiedEnum<Comparator, String>,
    val comparisonValue: String,
    val value: T?,
    @Serializable(with = SegmentComparator.CodifiedSerializer::class)
    val segmentComparator: CodifiedEnum<SegmentComparator, String>?,
    val segmentId: String?
) {
    companion object {
        private val logger = KotlinLogging.logger {}

        fun <T> withAddedComparisonValue(rule: RolloutRule<T>, value: String): RolloutRule<T> {
            if (value.isBlank()) {
                logger.warn { "Comparison value is blank, returning original rule" }
                return rule
            }

            val values = rule.comparisonValue.split(",").filter { it.isNotBlank() }
            val updatedValues = (values + value.trim()).distinct()
            return rule.copy(comparisonValue = updatedValues.joinToString(","))
        }
    }
}

fun <T> rolloutRule(customize: RolloutRuleBuilder<T>.() -> Unit): RolloutRule<T> {
    return RolloutRuleBuilder<T>().apply(customize).build()
}

class RolloutRuleBuilder<T> {
    var comparisonAttribute: CodifiedEnum<ComparisonAttribute, String> = ComparisonAttribute.Identifier.codifiedEnum()
    var comparator: CodifiedEnum<Comparator, String> = Comparator.SensitiveIsOneOf.codifiedEnum()
    var comparisonValue: String = ""
    var value: T? = null
    var segmentComparator: CodifiedEnum<SegmentComparator, String>? = null
    var segmentId: String? = null

    fun build(): RolloutRule<T> = RolloutRule(
        comparisonAttribute = comparisonAttribute,
        comparator = comparator,
        comparisonValue = comparisonValue,
        value = value,
        segmentComparator = segmentComparator,
        segmentId = segmentId
    )
}
