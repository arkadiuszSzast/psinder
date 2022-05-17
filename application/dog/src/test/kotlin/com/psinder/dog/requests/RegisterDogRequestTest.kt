package com.psinder.dog.requests

import com.psinder.dog.DogDescription
import com.psinder.dog.DogName
import com.psinder.shared.validation.validate
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.arrow.isInvalid
import strikt.assertions.hasSize

class RegisterDogRequestTest : DescribeSpec() {

    init {

        describe("RegisterDogRequest") {

            it("should failed validation when dog name and description are to short") {
                // arrange
                val request = RegisterDogRequest(DogName("pu"), DogDescription("short"), emptyList())

                // act
                val result = request.validate()

                // assert
                expectThat(result).isInvalid()
                    .get { value.first().validationErrors }.hasSize(2)
            }
        }
    }
}
