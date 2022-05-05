package com.psinder.shared.provider

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isNotSameInstanceAs

class ProviderTest : DescribeSpec() {

    init {

        describe("Provider") {

            it("should return different instances") {
                // arrange
                val fooProvider = Provider { Foo(1) }

                // act
                val fooA = fooProvider.get()
                val fooB = fooProvider.get()

                // assert
                expectThat(fooA)
                    .isNotSameInstanceAs(fooB)
            }
        }
    }
}

private data class Foo(val value: Int)
