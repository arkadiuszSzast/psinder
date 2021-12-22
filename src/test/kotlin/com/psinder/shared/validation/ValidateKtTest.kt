package com.psinder.shared.validation

import arrow.core.NonEmptyList
import arrow.core.nel
import arrow.core.nonEmptyListOf
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isEqualTo
import strikt.assertions.size

class ValidateKtTest : DescribeSpec({

    describe("merging validation errors to exception") {

        it("when only single error") {
            //arrange
            val errors = ValidationError("error_1").nel()

            //act
            val result = errors.mergeToException()

            //assert
            expectThat(result)
                .get { validationErrorCodes }
                .containsExactly("error_1")
        }

        it("when two errors") {
            //arrange
            val errors = nonEmptyListOf(ValidationError("error_1"), ValidationError("error_2"))

            //act
            val result = errors.mergeToException()

            //assert
            expectThat(result)
                .get { validationErrorCodes }
                .containsExactly("error_1", "error_2")
        }

        it("when ten errors") {
            //arrange
            val errors = IntRange(1, 10).map { ValidationError("error_$it") }.let { NonEmptyList.fromListUnsafe(it) }

            //act
            val result = errors.mergeToException()

            //assert
            expectThat(result)
                .get { validationErrorCodes }
                .and { size.isEqualTo(10) }
                .and { containsExactly(IntRange(1,10).map { "error_$it" }) }
        }
    }
})
