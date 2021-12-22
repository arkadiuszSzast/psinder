package com.psinder.account

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.arrow.isInvalid
import strikt.arrow.isValid
import strikt.assertions.*

class RegisterRequestTest : DescribeSpec({

    describe("create RegisterRequest object") {
        val validUsername = "Joe"
        val validEmail = "joe@doe.com"
        val validPassword = "12345678901#"

        describe("should throw exception when") {

            it("username is empty") {
                expectThat(RegisterRequest.create(" ", validEmail, validPassword))
                    .isInvalid()
                    .get { value.map { it.validationErrorCode } }
                    .containsExactly("validation.blank_username")

                expectThat(RegisterRequest.create("", validEmail, validPassword))
                    .isInvalid()
                    .get { value.map { it.validationErrorCode } }
                    .containsExactly("validation.blank_username")
            }

            it("invalid email format") {
                expectThat(RegisterRequest.create(validUsername, "invalid_email", validPassword))
                    .isInvalid()
                    .get { value.map { it.validationErrorCode } }
                    .containsExactly("validation.invalid_email_format")
            }

            it("too short password") {
                expectThat(RegisterRequest.create(validUsername, validEmail, "123#"))
                    .isInvalid()
                    .get { value.map { it.validationErrorCode } }
                    .containsExactly("validation.password_too_short")
            }

            it("all fields are invalid") {
                expectThat(RegisterRequest.create(" ", "invalid_email", "\t"))
                    .isInvalid()
                    .get { value.map { it.validationErrorCode } }
                    .containsExactlyInAnyOrder(
                        "validation.blank_username",
                        "validation.invalid_email_format",
                        "validation.password_cannot_be_blank",
                        "validation.password_too_short",
                        "validation.password_must_contains_number",
                        "validation.password_must_contains_special_character",
                        "validation.password_cannot_have_whitespaces"
                    )
            }
        }

        describe("trimming values") {

            it("should trim username and email") {
                val result = RegisterRequest.create(" Joe ", " joe@doe.com ", validPassword)

                expectThat(result)
                    .isValid()
                    .get { value }
                    .and { get { username.value }.not().startsWith(" ").not().endsWith(" ") }
                    .and { get { emailAddress.value }.not().startsWith(" ").not().endsWith(" ") }
            }
        }
    }

})
