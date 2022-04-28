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
                //arrange
                val rules = BooleanRolloutRules.create(emptyList())

                //act
                val result = rules.enableForUser(FeatureToggleUserIdentifier("user-id-1"))

                //assert
                expectThat(result) {
                    hasSize(1)
                    first().get { comparisonValue }.isEqualTo("user-id-1")
                }
            }

            it("should enable for user when no user specific enabled rule is present") {
                //arrange
                val rule = rolloutRule<Boolean> { value = false }
                val rules = BooleanRolloutRules.create(rule.nel())

                //act
                val result = rules.enableForUser(FeatureToggleUserIdentifier("user-id-1"))
                expectThat(result) {
                    hasSize(2)
                    first { it.value == true }.get { comparisonValue }.isEqualTo("user-id-1")
                }
            }

            it("should add new value to the list") {
                //arrange
                val rule = rolloutRule<Boolean> { value = true; comparisonValue = "user-id-1" }
                val rules = BooleanRolloutRules.create(rule.nel())

                //act
                val result = rules.enableForUser(FeatureToggleUserIdentifier("user-id-2"))
                expectThat(result) {
                    hasSize(1)
                    first { it.value == true }.get { comparisonValue }.isEqualTo("user-id-1,user-id-2")
                }
            }
        }
    }
}