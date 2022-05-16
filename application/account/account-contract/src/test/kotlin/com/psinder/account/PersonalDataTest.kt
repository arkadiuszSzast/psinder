package com.psinder.account

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class PersonalDataTest : DescribeSpec({

    describe("create name") {

        it("should start with uppercase") {
            // arrange && act && assert
            expectThat(Name.create("joe"))
                .get { value }
                .isEqualTo("Joe")

            // arrange && act && assert
            expectThat(Name.create("joe"))
                .get { value }
                .isEqualTo("Joe")
        }

        it("should trim") {
            // arrange && act && assert
            expectThat(Name.create(" Joe "))
                .get { value }
                .isEqualTo("Joe")
        }
    }

    describe("create surname") {

        it("should start with uppercase") {
            // arrange && act && assert
            expectThat(Surname.create("doe"))
                .get { value }
                .isEqualTo("Doe")

            // arrange && act && assert
            expectThat(Surname.create("doe"))
                .get { value }
                .isEqualTo("Doe")
        }

        it("should trim") {
            // arrange && act && assert
            expectThat(Surname.create(" Doe "))
                .get { value }
                .isEqualTo("Doe")
        }
    }
})
