package com.psinder.feature.toggle

import arrow.core.None
import arrow.core.Some
import arrow.core.toOption

interface BooleanRolloutRules : List<RolloutRule<Boolean>> {
    companion object {
        fun create(rules: List<RolloutRule<Boolean>>): BooleanRolloutRules = Simple(rules)
    }

    fun enableForUser(userIdentifier: FeatureToggleUserIdentifier): BooleanRolloutRules =
        when (val rule = findEnabledForUsersRole()) {
            is Some -> {
                val updatedRule = RolloutRule.withAddedComparisonValue(rule.value, userIdentifier.userId)
                Simple(this.filter { it != rule.value } + updatedRule)
            }
            is None -> {
                val emptyRule = rolloutRule<Boolean> {
                    value = true
                }
                val updatedRule = RolloutRule.withAddedComparisonValue(emptyRule, userIdentifier.userId)
                Simple(this + updatedRule)
            }
        }

    private fun findEnabledForUsersRole() =
        this.filterIsInstance<RolloutRule<Boolean>>().find {
            it.value == true &&
                it.comparator.code() == Comparator.SensitiveIsOneOf.code &&
                it.comparisonAttribute.code() == ComparisonAttribute.Identifier.code
        }.toOption()
}

private class Simple(private val rules: List<RolloutRule<Boolean>>) :
    List<RolloutRule<Boolean>> by rules,
    BooleanRolloutRules
