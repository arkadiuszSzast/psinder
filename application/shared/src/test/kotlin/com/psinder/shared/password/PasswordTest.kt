package com.psinder.shared.password

import com.psinder.shared.validation.mergeAll
import com.psinder.shared.validation.validate
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import org.mindrot.jbcrypt.BCrypt
import strikt.api.expectThat
import strikt.arrow.isInvalid
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.isEqualTo

class PasswordTest : DescribeSpec({

    describe("create raw password object") {

        it("created object should store raw password") {
            val notHashedPassword = "super_secret"
            val password = RawPassword(notHashedPassword)

            expectThat(password)
                .get { value }
                .isEqualTo(notHashedPassword)
        }

        it("hashing password") {
            val notHashedPassword = "super_secret"
            val password = RawPassword(notHashedPassword).hashpw()

            expectThat(password)
                .get { value }
                .not().isEqualTo(notHashedPassword)
                .and { BCrypt.checkpw(notHashedPassword, this.subject).shouldBeTrue() }
        }
    }

    describe("default validation rules") {
        val password = RawPassword("\t").validate()

        expectThat(password)
            .isInvalid()
            .get { value.mergeAll().validationErrors.map { it.message } }
            .containsExactlyInAnyOrder(
                ("validation.password_too_short"),
                ("validation.password_must_contains_number"),
                ("validation.password_must_contains_special_character"),
                ("validation.password_cannot_have_whitespaces")
            )
    }
})
