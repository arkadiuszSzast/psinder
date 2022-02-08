package com.psinder.shared

import com.psinder.shared.validation.mergeAll
import com.psinder.shared.validation.validate
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.arrow.isInvalid
import strikt.arrow.isValid
import strikt.assertions.containsExactly
import strikt.assertions.endsWith
import strikt.assertions.isEqualTo
import strikt.assertions.startsWith

class EmailTest : DescribeSpec({

    describe("create email object") {

        it("should be valid") {
            val emailAddress = EmailAddress.create("joe@doe.com").validate()

            expectThat(emailAddress)
                .isValid()
                .get { value }
                .get { value }
                .isEqualTo("joe@doe.com")
        }

        it("address should be trimmed") {
            val emailAddress = EmailAddress.create(" joe@doe.com ")

            expectThat(emailAddress)
                .get { value }
                .not().startsWith(" ")
                .not().endsWith(" ")
        }

        describe("validations") {

            it("invalid format") {
                val emailAddress = EmailAddress.create("invalid_email").validate()

                expectThat(emailAddress)
                    .isInvalid()
                    .get { value.mergeAll().validationErrors.map { it.message } }
                    .containsExactly("validation.invalid_email_format")
            }
        }
    }
})
