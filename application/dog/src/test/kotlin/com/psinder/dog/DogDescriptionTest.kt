package com.psinder.dog

import com.psinder.shared.validation.validate
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import strikt.api.expectThat
import strikt.arrow.isInvalid
import strikt.arrow.isValid
import strikt.assertions.isEqualTo

class DogDescriptionTest : DescribeSpec() {

    init {

        describe("DogDescription") {

            it("should failed validation when less than 10 characters") {
                //arrange
                val dogDescription = DogDescription("short")

                //act
                val result = dogDescription.validate()

                //assert
                expectThat(result).isInvalid()
            }

            it("should pass validation when exactly 10 characters") {
                //arrange
                val dogDescription = DogDescription("1234567890")

                //act
                val result = dogDescription.validate()

                //assert
                expectThat(result).isValid()
            }

            it("should pass validation when more than 10 characters") {
                //arrange
                val dogDescription = DogDescription("12345678901")

                //act
                val result = dogDescription.validate()

                //assert
                expectThat(result).isValid()
            }

        }
    }
}
