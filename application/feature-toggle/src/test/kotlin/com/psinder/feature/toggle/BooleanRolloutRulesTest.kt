package com.psinder.feature.toggle

import arrow.core.nel
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.first
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo

class BooleanRolloutRulesTest : DescribeSpec() {

    init {

        describe("BooleanRolloutRules") {

            it("should enable for user when rollout rules list is empty") {
                // arrange
                val rules = BooleanRolloutRules.create(emptyList())

                // act
                val result = rules.enableForUser(FeatureToggleUserIdentifier("user-id-1"))

                // assert
                expectThat(result) {
                    hasSize(1)
                    first().get { value }.isEqualTo(true)
                    first().get { comparisonValue }.isEqualTo("user-id-1")
                }
            }

            it("should enable for user when no user specific enabled rule is present") {
                // arrange
                val rule = rolloutRule<Boolean> { value = false }
                val rules = BooleanRolloutRules.create(rule.nel())

                // act
                val result = rules.enableForUser(FeatureToggleUserIdentifier("user-id-1"))
                expectThat(result) {
                    hasSize(2)
                    first { it.value == true }.get { comparisonValue }.isEqualTo("user-id-1")
                }
            }

            it("should enable") {
                // arrange
                val rule = rolloutRule<Boolean> { value = true; comparisonValue = "user-id-1" }
                val rules = BooleanRolloutRules.create(rule.nel())

                // act
                val result = rules.enableForUser(FeatureToggleUserIdentifier("user-id-2"))

                // assert
                expectThat(result) {
                    hasSize(1)
                    first { it.value == true }.get { comparisonValue }.isEqualTo("user-id-1,user-id-2")
                }
            }

            it("should disable for user when rollout rules list is empty") {
                // arrange
                val rules = BooleanRolloutRules.create(emptyList())

                // act
                val result = rules.disableForUser(FeatureToggleUserIdentifier("user-id-1"))

                // assert
                expectThat(result) {
                    hasSize(1)
                    first().get { value }.isEqualTo(false)
                    first().get { comparisonValue }.isEqualTo("user-id-1")
                }
            }

            it("should disable for user when no user specific enabled rule is present") {
                // arrange
                val rule = rolloutRule<Boolean> { value = true }
                val rules = BooleanRolloutRules.create(rule.nel())

                // act
                val result = rules.disableForUser(FeatureToggleUserIdentifier("user-id-1"))
                expectThat(result) {
                    hasSize(2)
                    first { it.value == false }.get { comparisonValue }.isEqualTo("user-id-1")
                }
            }

            it("should disable") {
                // arrange
                val rule = rolloutRule<Boolean> { value = false; comparisonValue = "user-id-1" }
                val rules = BooleanRolloutRules.create(rule.nel())

                // act
                val result = rules.disableForUser(FeatureToggleUserIdentifier("user-id-2"))

                // assert
                expectThat(result) {
                    hasSize(1)
                    first { it.value == false }.get { comparisonValue }.isEqualTo("user-id-1,user-id-2")
                }
            }
        }
    }
}
