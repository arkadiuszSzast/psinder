package com.psinder.feature.toggle

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class RolloutRuleTest : DescribeSpec() {

    init {

        describe("RolloutRule") {

            it("should add comparison value when current list is empty") {
                // arrange
                val rule = rolloutRule<Boolean> {
                    value = true
                    comparisonValue = ""
                }

                // act
                val result = RolloutRule.withAddedComparisonValue(rule, "123")

                // assert
                expectThat(result) {
                    get { value }.isEqualTo(true)
                    get { comparisonValue }.isEqualTo("123")
                }
            }

            it("should append comparison value when current list is not empty") {
                // arrange
                val rule = rolloutRule<Boolean> {
                    value = true
                    comparisonValue = "1,2,3"
                }

                // act
                val result = RolloutRule.withAddedComparisonValue(rule, "123")

                // assert
                expectThat(result) {
                    get { value }.isEqualTo(true)
                    get { comparisonValue }.isEqualTo("1,2,3,123")
                }
            }

            it("should not add new value when already present") {
                // arrange
                val rule = rolloutRule<Boolean> {
                    value = true
                    comparisonValue = "1,2,3"
                }

                // act
                val result = RolloutRule.withAddedComparisonValue(rule, "2")

                // assert
                expectThat(result) {
                    get { value }.isEqualTo(true)
                    get { comparisonValue }.isEqualTo("1,2,3")
                }
            }

            it("should trim the value") {
                // arrange
                val rule = rolloutRule<Boolean> {
                    value = true
                    comparisonValue = "1,2,3"
                }

                // act
                val result = RolloutRule.withAddedComparisonValue(rule, " 4 ")

                // assert
                expectThat(result) {
                    get { value }.isEqualTo(true)
                    get { comparisonValue }.isEqualTo("1,2,3,4")
                }
            }

            it("when given value is empty return original rule") {
                // arrange
                val rule = rolloutRule<Boolean> {
                    value = true
                    comparisonValue = "1,2,3"
                }

                // act
                val result = RolloutRule.withAddedComparisonValue(rule, "")

                // assert
                expectThat(result) {
                    get { value }.isEqualTo(true)
                    get { comparisonValue }.isEqualTo("1,2,3")
                }
            }

            it("when given value contains only whitespace return original rule") {
                // arrange
                val rule = rolloutRule<Boolean> {
                    value = true
                    comparisonValue = "1,2,3"
                }

                // act
                val result = RolloutRule.withAddedComparisonValue(rule, "  ")

                // assert
                expectThat(result) {
                    get { value }.isEqualTo(true)
                    get { comparisonValue }.isEqualTo("1,2,3")
                }
            }
        }
    }
}
