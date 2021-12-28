package com.psinder.shared.password

import com.psinder.shared.validation.mergeAll
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import org.mindrot.jbcrypt.BCrypt
import strikt.api.expectThat
import strikt.arrow.isInvalid
import strikt.arrow.isValid
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.isEqualTo

class PasswordTest : DescribeSpec({

    describe("create password object") {

        it("created object should store hashed password") {
            val notHashedPassword = "super_secret"
            val password = Password.create(notHashedPassword, emptyList())

            expectThat(password)
                .isValid()
                .get { value }
                .get { value }
                .not().isEqualTo(notHashedPassword)
                .and { BCrypt.checkpw(notHashedPassword, this.subject).shouldBeTrue() }
        }
    }

    describe("default validation rules") {
        val password = Password.create("\t")

        expectThat(password)
            .isInvalid()
            .get { value.mergeAll().validationErrorCodes }
            .containsExactlyInAnyOrder(
                ("validation.password_cannot_be_blank"),
                ("validation.password_too_short"),
                ("validation.password_must_contains_number"),
                ("validation.password_must_contains_special_character"),
                ("validation.password_cannot_have_whitespaces")
            )
    }
})
