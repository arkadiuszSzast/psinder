package com.psinder.shared.validation.rules

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.konform.validation.Validation
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.first
import strikt.assertions.hasSize
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo

class ValidationRulesKtTest : DescribeSpec() {

    init {

        describe("ValidationRules") {

            it("should pass isValidJwt check") {
                // arrange
                val validJwt = JWT.create().sign(Algorithm.HMAC256("secret"))
                val validator = Validation<String> {
                    isValidJwt()
                }

                // act && assert
                expectThat(validator.validate(validJwt))
                    .get { errors }.isEmpty()
            }

            it("should fail isValidJwt check") {
                // arrange
                val invalidJwt = "abc"
                val validator = Validation<String> {
                    isValidJwt()
                }

                // act && assert
                expectThat(validator.validate(invalidJwt))
                    .get { errors }.hasSize(1)
                    .first().get { this.message }.isEqualTo("not a valid JWT token")
            }

            it("should pass cannotHaveWhitespaces check") {
                // arrange
                val value = "abc"
                val validator = Validation<String> {
                    cannotHaveWhitespaces()
                }

                // act && assert
                expectThat(validator.validate(value))
                    .get { errors }.isEmpty()
            }

            it("should fail cannotHaveWhitespaces check") {
                // arrange
                val value = "a bc"
                val validator = Validation<String> {
                    cannotHaveWhitespaces()
                }

                // act && assert
                expectThat(validator.validate(value))
                    .get { errors }.hasSize(1)
                    .first().get { this.message }.isEqualTo("cannot contains whitespace character")
            }

            it("should pass cannotEndsWith check") {
                // arrange
                val value = "abc"
                val validator = Validation<String> {
                    cannotEndsWith("b")
                }

                // act && assert
                expectThat(validator.validate(value))
                    .get { errors }.isEmpty()
            }

            it("should fail cannotEndsWith check") {
                // arrange
                val value = "abc"
                val validator = Validation<String> {
                    cannotEndsWith("c")
                }

                // act && assert
                expectThat(validator.validate(value))
                    .get { errors }.hasSize(1)
                    .first().get { this.message }.isEqualTo("cannot ends with c")
            }

            it("should pass cannotStartsWith check") {
                // arrange
                val value = "abc"
                val validator = Validation<String> {
                    cannotStartsWith("b")
                }

                // act && assert
                expectThat(validator.validate(value))
                    .get { errors }.isEmpty()
            }

            it("should fail cannotStartsWith check") {
                // arrange
                val value = "abc"
                val validator = Validation<String> {
                    cannotStartsWith("a")
                }

                // act && assert
                expectThat(validator.validate(value))
                    .get { errors }.hasSize(1)
                    .first().get { this.message }.isEqualTo("cannot starts with a")
            }

            it("should pass containsSpecialCharacter check") {
                // arrange
                val value = "abc%"
                val validator = Validation<String> {
                    containsSpecialCharacter()
                }

                // act && assert
                expectThat(validator.validate(value))
                    .get { errors }.isEmpty()
            }

            it("should fail containsSpecialCharacter check") {
                // arrange
                val value = "abc"
                val validator = Validation<String> {
                    containsSpecialCharacter()
                }

                // act && assert
                expectThat(validator.validate(value))
                    .get { errors }.hasSize(1)
                    .first().get { this.message }.isEqualTo("must contains a special character")
            }

            it("should pass containsNumber check") {
                // arrange
                val value = "abc1"
                val validator = Validation<String> {
                    containsNumber()
                }

                // act && assert
                expectThat(validator.validate(value))
                    .get { errors }.isEmpty()
            }

            it("should fail containsNumber check") {
                // arrange
                val value = "abc"
                val validator = Validation<String> {
                    containsNumber()
                }

                // act && assert
                expectThat(validator.validate(value))
                    .get { errors }.hasSize(1)
                    .first().get { this.message }.isEqualTo("must contains a number")
            }
        }
    }
}
