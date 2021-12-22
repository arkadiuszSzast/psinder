package com.psinder.shared

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isEmpty

class OptionKtTest : DescribeSpec({

    describe("filtering empty options from collection") {

        it("should not remove anything when no empty options") {
            val options = listOf(Some(1), Some(2), Some(3))

            val result = options.allNotEmpty()

            expectThat(result)
                .containsExactly(1,2,3)
        }

        it("should remove empty options") {
            val options = listOf(Some(1), Some(2), None)

            val result = options.allNotEmpty()

            expectThat(result)
                .containsExactly(1,2)
        }

        it("return empty list when all items are empty") {
            expectThat(listOf(None).allNotEmpty())
                .isEmpty()

            expectThat(listOf(None, None, None).allNotEmpty())
                .isEmpty()
        }

        it("return empty list when empty list given") {
            expectThat(emptyList<Option<Int>>().allNotEmpty())
                .isEmpty()
        }
    }

})
