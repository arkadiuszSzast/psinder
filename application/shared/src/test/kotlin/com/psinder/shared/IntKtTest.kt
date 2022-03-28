package com.psinder.shared

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isFalse
import strikt.assertions.isTrue

class IntKtTest : DescribeSpec({

    describe("isBetween") {

        describe("should return true") {

            it("when in range") {
                val result = 5.isBetween(1, 10)

                expectThat(result)
                    .isTrue()
            }

            it("when equal to min") {
                val result = 5.isBetween(5, 10)

                expectThat(result)
                    .isTrue()
            }

            it("when equal to max") {
                val result = 5.isBetween(1, 5)

                expectThat(result)
                    .isTrue()
            }
        }

        describe("should return false") {

            it("when less") {
                val result = 1.isBetween(5, 10)

                expectThat(result)
                    .isFalse()
            }

            it("when more") {
                val result = 10.isBetween(1, 5)

                expectThat(result)
                    .isFalse()
            }
        }
    }
})
