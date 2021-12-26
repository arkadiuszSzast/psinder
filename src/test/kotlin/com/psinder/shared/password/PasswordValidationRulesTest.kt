package com.psinder.shared.password

import com.psinder.shared.validation.checkAll
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.contains

class PasswordValidationRulesTest : DescribeSpec({

    describe("default password validation rules") {
        val rules = defaultPasswordValidationRules

        it("password cannot be blank") {
            expectThat(rules.checkAll(""))
                .get { flatMap { it.validationErrorCodes } }
                .contains("validation.password_cannot_be_blank")

            expectThat(rules.checkAll(" "))
                .get { flatMap { it.validationErrorCodes } }
                .contains("validation.password_cannot_be_blank")

            expectThat(rules.checkAll("test"))
                .get { flatMap { it.validationErrorCodes } }
                .not().contains("validation.password_cannot_be_blank")
        }

        it("password has at least 12 characters") {
            expectThat(rules.checkAll("12345678901"))
                .get { flatMap { it.validationErrorCodes } }
                .contains("validation.password_too_short")

            expectThat(rules.checkAll("123456789012"))
                .get { flatMap { it.validationErrorCodes } }
                .not().contains("validation.password_too_short")

            expectThat(rules.checkAll("1234567890123"))
                .get { flatMap { it.validationErrorCodes } }
                .not().contains("validation.password_too_short")
        }

        it("password contains number") {
            expectThat(rules.checkAll("ABC"))
                .get { flatMap { it.validationErrorCodes } }
                .contains("validation.password_must_contains_number")

            expectThat(rules.checkAll("ABC1"))
                .get { flatMap { it.validationErrorCodes } }
                .not().contains("validation.password_must_contains_number")

            expectThat(rules.checkAll("1"))
                .get { flatMap { it.validationErrorCodes } }
                .not().contains("validation.password_must_contains_number")
        }

        it("password contains special character") {
            expectThat(rules.checkAll("ABC"))
                .get { flatMap { it.validationErrorCodes } }
                .contains("validation.password_must_contains_special_character")

            expectThat(rules.checkAll("ABC#"))
                .get { flatMap { it.validationErrorCodes } }
                .not().contains("validation.password_must_contains_special_character")

            expectThat(rules.checkAll("#"))
                .get { flatMap { it.validationErrorCodes } }
                .not().contains("validation.password_must_contains_special_character")
        }

        it("password cannot contains whitespace") {
            expectThat(rules.checkAll(" A"))
                .get { flatMap { it.validationErrorCodes } }
                .contains("validation.password_cannot_have_whitespaces")

            expectThat(rules.checkAll("AB \tC"))
                .get { flatMap { it.validationErrorCodes } }
                .contains("validation.password_cannot_have_whitespaces")

            expectThat(rules.checkAll("ABC "))
                .get { flatMap { it.validationErrorCodes } }
                .contains("validation.password_cannot_have_whitespaces")

            expectThat(rules.checkAll("ABC"))
                .get { flatMap { it.validationErrorCodes } }
                .not().contains("validation.password_cannot_have_whitespaces")
        }
    }
})
