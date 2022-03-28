package com.psinder.shared.response

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isFalse
import strikt.assertions.isTrue

class ResponseCodeTest : DescribeSpec({

    describe("check is success") {

        it("true for 200") {
            val result = ResponseCode(200).isSuccess

            expectThat(result)
                .isTrue()
        }

        it("true for 299") {
            val result = ResponseCode(299).isSuccess

            expectThat(result)
                .isTrue()
        }

        it("false for 300") {
            val result = ResponseCode(300).isSuccess

            expectThat(result)
                .isFalse()
        }

        it("false for 400") {
            val result = ResponseCode(300).isSuccess

            expectThat(result)
                .isFalse()
        }
    }
})
