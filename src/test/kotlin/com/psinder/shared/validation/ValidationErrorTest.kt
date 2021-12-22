package com.psinder.shared.validation

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isA

class ValidationErrorTest : DescribeSpec({

    describe("validation error") {

        it("can transform to throwable") {
            val error = ValidationError("error_1")

            expectThat(error.toThrowable())
                .isA<ValidationException>()
                .get { validationErrorCodes }
                .containsExactly("error_1")
        }
    }
})
