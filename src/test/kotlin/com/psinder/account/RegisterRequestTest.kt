package com.psinder.account

import com.psinder.account.requests.CreateAccountRequest
import com.psinder.shared.EmailAddress
import com.psinder.shared.password.RawPassword
import com.psinder.shared.validation.mergeAll
import com.psinder.shared.validation.validate
import io.kotest.core.spec.style.DescribeSpec
import kotlinx.datetime.TimeZone
import strikt.api.expectThat
import strikt.arrow.isInvalid
import strikt.arrow.isValid
import strikt.assertions.containsExactly
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.endsWith
import strikt.assertions.startsWith

class RegisterRequestTest : DescribeSpec({

    describe("validate RegisterRequest object") {
        val validPersonalData = PersonalData(Name.create("Joe"), Surname.create("Doe"))
        val validEmail = EmailAddress.create("joe@doe.com")
        val validPassword = RawPassword("12345678901#")
        val validTimeZone = TimeZone.currentSystemDefault()

        describe("should throw exception when") {

            it("invalid email format") {
                expectThat(
                    CreateAccountRequest(
                        validPersonalData,
                        EmailAddress.create("invalid_email"),
                        validPassword, validTimeZone
                    ).validate()
                )
                    .isInvalid()
                    .get { value.mergeAll().validationErrors.map { it.message } }
                    .containsExactly("validation.invalid_email_format")
            }

            it("too short password") {
                expectThat(
                    CreateAccountRequest(
                        validPersonalData,
                        validEmail,
                        RawPassword("123#"),
                        validTimeZone
                    ).validate()
                )
                    .isInvalid()
                    .get { value.mergeAll().validationErrors.map { it.message } }
                    .containsExactly("validation.password_too_short")
            }

            it("all fields are invalid") {
                expectThat(
                    CreateAccountRequest(
                        validPersonalData,
                        EmailAddress.create("invalid_email"),
                        RawPassword("\t"),
                        validTimeZone
                    ).validate()
                )
                    .isInvalid()
                    .get { value.mergeAll().validationErrors.map { it.message } }
                    .containsExactlyInAnyOrder(
                        "validation.invalid_email_format",
                        "validation.password_too_short",
                        "validation.password_must_contains_number",
                        "validation.password_must_contains_special_character",
                        "validation.password_cannot_have_whitespaces"
                    )
            }
        }

        describe("trimming values") {

            it("should trim email") {
                val result = CreateAccountRequest(
                    validPersonalData,
                    EmailAddress.create(" joe@doe.com "),
                    validPassword,
                    validTimeZone
                ).validate()

                expectThat(result)
                    .isValid()
                    .get { value }
                    .and { get { email.value }.not().startsWith(" ").not().endsWith(" ") }
            }
        }
    }
})
