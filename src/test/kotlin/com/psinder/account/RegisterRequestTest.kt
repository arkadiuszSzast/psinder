package com.psinder.account

import com.psinder.account.requests.CreateAccountRequest
import com.psinder.shared.validation.mergeAll
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.arrow.isInvalid
import strikt.arrow.isValid
import strikt.assertions.containsExactly
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.endsWith
import strikt.assertions.startsWith
import java.time.ZoneId

class RegisterRequestTest : DescribeSpec({

    describe("create RegisterRequest object") {
        val validPersonalData = PersonalData(Name.create("Joe"), Surname.create("Doe"))
        val validEmail = "joe@doe.com"
        val validPassword = "12345678901#"
        val validZoneId = ZoneId.systemDefault()

        describe("should throw exception when") {

            it("invalid email format") {
                expectThat(CreateAccountRequest.create(validPersonalData, "invalid_email", validPassword, validZoneId))
                    .isInvalid()
                    .get { value.mergeAll().validationErrorCodes }
                    .containsExactly("validation.invalid_email_format")
            }

            it("too short password") {
                expectThat(CreateAccountRequest.create(validPersonalData, validEmail, "123#", validZoneId))
                    .isInvalid()
                    .get { value.mergeAll().validationErrorCodes }
                    .containsExactly("validation.password_too_short")
            }

            it("all fields are invalid") {
                expectThat(CreateAccountRequest.create(validPersonalData, "invalid_email", "\t", validZoneId))
                    .isInvalid()
                    .get { value.mergeAll().validationErrorCodes }
                    .containsExactlyInAnyOrder(
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

            it("should trim email") {
                val result = CreateAccountRequest.create(validPersonalData, " joe@doe.com ", validPassword, validZoneId)

                expectThat(result)
                    .isValid()
                    .get { value }
                    .and { get { email.value }.not().startsWith(" ").not().endsWith(" ") }
            }
        }
    }
})
