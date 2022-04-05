package com.psinder.account

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class AddressDataTest : DescribeSpec({

    describe("create city") {

        it("should start with uppercase") {
            // arrange && act && assert
            expectThat(City.create("Warsaw"))
                .get { value }
                .isEqualTo("Warsaw")

            // arrange && act && assert
            expectThat(City.create("warsaw"))
                .get { value }
                .isEqualTo("Warsaw")
        }

        it("should trim") {

            // arrange && act && assert
            expectThat(City.create(" Warsaw "))
                .get { value }
                .isEqualTo("Warsaw")
        }
    }

    describe("create street name") {

        it("should start with uppercase") {
            // arrange && act && assert
            expectThat(StreetName.create("Wall Street"))
                .get { value }
                .isEqualTo("Wall Street")

            // arrange && act && assert
            expectThat(StreetName.create("wall Street"))
                .get { value }
                .isEqualTo("Wall Street")
        }

        it("works correctly when starts with number") {
            // arrange && act && assert
            expectThat(StreetName.create("100 wall Street"))
                .get { value }
                .isEqualTo("100 wall Street")
        }

        it("should trim") {
            // arrange && act && assert
            expectThat(StreetName.create(" Wall Street "))
                .get { value }
                .isEqualTo("Wall Street")
        }
    }
})
