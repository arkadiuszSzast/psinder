package com.psinder.account

import com.psinder.shared.validation.mergeAll
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.arrow.isInvalid
import strikt.arrow.isValid
import strikt.assertions.containsExactly
import strikt.assertions.endsWith
import strikt.assertions.startsWith

class UsernameTest : DescribeSpec({

    describe("create username object") {

        it("should trim") {
            val username = Username.create(" ABC ")

            expectThat(username)
                .isValid()
                .get { value }
                .get { value }
                .not().startsWith(" ")
                .not().endsWith(" ")
        }
    }

    describe("validations") {

        it("cannot create blank username") {

            expectThat(Username.create(""))
                .isInvalid()
                .get { value.mergeAll().validationErrorCodes }
                .containsExactly("validation.blank_username")

            expectThat(Username.create(" "))
                .isInvalid()
                .get { value.mergeAll().validationErrorCodes }
                .containsExactly("validation.blank_username")
        }
    }

})
