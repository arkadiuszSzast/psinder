package com.psinder.dog

import com.psinder.shared.validation.validate
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.arrow.isInvalid
import strikt.arrow.isValid
import strikt.assertions.isEqualTo

class DogNameTest : DescribeSpec() {

    init {

        describe("DogName") {

            it("should start with a capital letter") {
                // arrange && act
                val dogName = DogName("puppy")

                // assert
                expectThat(dogName.value).isEqualTo("Puppy")
            }

            it("should trim") {
                // arrange && act
                val dogName = DogName(" puppy ")

                // assert
                expectThat(dogName.value).isEqualTo("Puppy")
            }

            it("should fail validation if less than 3 characters") {
                // arrange
                val dogName = DogName("pu")

                // act
                val result = dogName.validate()

                // assert
                expectThat(result).isInvalid()
            }

            it("should pass validation when exactly 3 characters") {
                // arrange
                val dogName = DogName("pup")

                // act
                val result = dogName.validate()

                // assert
                expectThat(result).isValid()
            }

            it("should pass validation when more than 3 characters") {
                // arrange
                val dogName = DogName("pupp")

                // act
                val result = dogName.validate()

                // assert
                expectThat(result).isValid()
            }
        }
    }
}
