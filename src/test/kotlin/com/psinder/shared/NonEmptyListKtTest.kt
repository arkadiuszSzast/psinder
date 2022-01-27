package com.psinder.shared

import arrow.core.nel
import arrow.core.nonEmptyListOf
import arrow.typeclasses.Semigroup
import com.psinder.shared.validation.ValidationException
import io.konform.validation.ValidationError
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isEqualTo

private data class SimpleValidationError(
    override val dataPath: String,
    override val message: String
) : ValidationError

class NonEmptyListKtTest : DescribeSpec({

    describe("reduce non empty list") {

        it("can reduce list with single element") {
            val errors = ValidationException(nonEmptyListOf(SimpleValidationError(".", "error_1"))).nel()

            val result = errors.reduce(ValidationException.semigroup)

            expectThat(result).get { validationErrors }.containsExactly(SimpleValidationError(".", "error_1"))
        }

        it("can reduce list with two elements") {
            val errors = nonEmptyListOf(
                ValidationException(nonEmptyListOf(SimpleValidationError(".", "error_1"))),
                ValidationException(nonEmptyListOf(SimpleValidationError(".", "error_2")))
            )

            val result = errors.reduce(ValidationException.semigroup)

            expectThat(result).get { validationErrors }
                .containsExactly(
                    SimpleValidationError(".", "error_1"),
                    SimpleValidationError(".", "error_2")
                )
        }

        it("can reduce list with two elements and with different size") {
            val errors = nonEmptyListOf(
                ValidationException(nonEmptyListOf(SimpleValidationError(".", "error_1"))),
                ValidationException(
                    nonEmptyListOf(
                        SimpleValidationError(".", "error_2"),
                        SimpleValidationError(".", "error_3")
                    )
                )
            )

            val result = errors.reduce(ValidationException.semigroup)

            expectThat(result).get { validationErrors }.containsExactly(
                SimpleValidationError(".", "error_1"),
                SimpleValidationError(".", "error_2"),
                SimpleValidationError(".", "error_3")
            )
        }
    }

    describe("reduce and map non empty list") {
        it("single element") {
            val errors = nonEmptyListOf(1)

            val result = errors.reduceMap(Semigroup.Companion.string()) { it.toString() }

            expectThat(result).isEqualTo("1")
        }

        it("two elements") {
            val errors = nonEmptyListOf(1, 2)

            val result = errors.reduceMap(Semigroup.Companion.string()) { it.toString() }

            expectThat(result).isEqualTo("12")
        }
    }
})
