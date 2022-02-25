package com.psinder.account

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class PersonalDataTest : DescribeSpec({

    describe("create name") {

        it("should start with uppercase") {
            expectThat(Name.create("Joe"))
                .get { value }
                .isEqualTo("Joe")

            expectThat(Name.create("joe"))
                .get { value }
                .isEqualTo("Joe")
        }

        it("should trim") {
            expectThat(Name.create(" Joe "))
                .get { value }
                .isEqualTo("Joe")
        }
    }

    describe("create surname") {

        it("should start with uppercase") {
            expectThat(Surname.create("Doe"))
                .get { value }
                .isEqualTo("Doe")

            expectThat(Surname.create("doe"))
                .get { value }
                .isEqualTo("Doe")
        }

        it("should trim") {
            expectThat(Surname.create(" Doe "))
                .get { value }
                .isEqualTo("Doe")
        }
    }
})
