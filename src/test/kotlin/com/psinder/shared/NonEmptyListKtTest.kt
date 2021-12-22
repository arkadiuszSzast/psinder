package com.psinder.shared

import arrow.core.nel
import arrow.core.nonEmptyListOf
import arrow.typeclasses.Semigroup
import com.psinder.shared.validation.ValidationException
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isEqualTo

class NonEmptyListKtTest : DescribeSpec({

    describe("reduce non empty list") {

        it("can reduce list with single element") {
            //arrange
            val errors = ValidationException(nonEmptyListOf("error_1")).nel()

            //act
            val result = errors.reduce(ValidationException.semigroup)

            //assert
            expectThat(result)
                .get { validationErrorCodes }
                .containsExactly("error_1")
        }

        it("can reduce list with two elements") {
            //arrange
            val errors = nonEmptyListOf(
                ValidationException(nonEmptyListOf("error_1")),
                ValidationException(nonEmptyListOf("error_2"))
            )

            //act
            val result = errors.reduce(ValidationException.semigroup)

            //assert
            expectThat(result)
                .get { validationErrorCodes }
                .containsExactly("error_1", "error_2")
        }

        it("can reduce list with two elements and with different size") {
            //arrange
            val errors = nonEmptyListOf(
                ValidationException(nonEmptyListOf("error_1")),
                ValidationException(nonEmptyListOf("error_2", "error_3"))
            )

            //act
            val result = errors.reduce(ValidationException.semigroup)

            //assert
            expectThat(result)
                .get { validationErrorCodes }
                .containsExactly("error_1", "error_2", "error_3")
        }
    }

    describe("reduce and map non empty list") {
        it("single element") {
            //arrange
            val errors = nonEmptyListOf(1)

            //act
            val result = errors.reduceMap(Semigroup.Companion.string()) { it.toString() }

            //assert
            expectThat(result)
                .isEqualTo("1")
        }

        it("two elements") {
            //arrange
            val errors = nonEmptyListOf(1, 2)

            //act
            val result = errors.reduceMap(Semigroup.Companion.string()) { it.toString() }

            //assert
            expectThat(result)
                .isEqualTo("12")
        }

    }

})
