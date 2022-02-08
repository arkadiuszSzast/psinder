package com.psinder.shared.password

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.contains

class PasswordValidationRulesTest : DescribeSpec({

    describe("default password validation rules") {
        val validator = defaultPasswordValidator

        it("password has at least 12 characters") {
            expectThat(validator.validate("12345678901"))
                .get { errors.map { it.message } }
                .contains("validation.password_too_short")

            expectThat(validator.validate("123456789012"))
                .get { errors.map { it.message } }
                .not().contains("validation.password_too_short")

            expectThat(validator.validate("1234567890123"))
                .get { errors.map { it.message } }
                .not().contains("validation.password_too_short")
        }

        it("password contains number") {
            expectThat(validator.validate("ABC"))
                .get { errors.map { it.message } }
                .contains("validation.password_must_contains_number")

            expectThat(validator.validate("ABC1"))
                .get { errors.map { it.message } }
                .not().contains("validation.password_must_contains_number")

            expectThat(validator.validate("1"))
                .get { errors.map { it.message } }
                .not().contains("validation.password_must_contains_number")
        }

        it("password contains special character") {
            expectThat(validator.validate("ABC"))
                .get { errors.map { it.message } }
                .contains("validation.password_must_contains_special_character")

            expectThat(validator.validate("ABC#"))
                .get { errors.map { it.message } }
                .not().contains("validation.password_must_contains_special_character")

            expectThat(validator.validate("#"))
                .get { errors.map { it.message } }
                .not().contains("validation.password_must_contains_special_character")
        }

        it("password cannot contains whitespace") {
            expectThat(validator.validate(" A"))
                .get { errors.map { it.message } }
                .contains("validation.password_cannot_have_whitespaces")

            expectThat(validator.validate("AB \tC"))
                .get { errors.map { it.message } }
                .contains("validation.password_cannot_have_whitespaces")

            expectThat(validator.validate("ABC "))
                .get { errors.map { it.message } }
                .contains("validation.password_cannot_have_whitespaces")

            expectThat(validator.validate("ABC"))
                .get { errors.map { it.message } }
                .not().contains("validation.password_cannot_have_whitespaces")
        }
    }
})
