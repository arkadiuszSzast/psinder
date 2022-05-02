package com.psinder.shared.map

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.containsKeys
import strikt.assertions.hasSize
import strikt.assertions.isEmpty

class FilterNotNullValuesKtTest : DescribeSpec() {

    init {
        describe("filterNotNullValues") {

            it("should filter out null values") {
                //arrange
                val mapWithNullValues = mapOf(
                    "key1" to null,
                    "key2" to null,
                    "key3" to "value3",
                    "key4" to "value4"
                )

                //act
                val result = mapWithNullValues.filterNotNullValues()

                //assert
                expectThat(result) {
                    and { hasSize(2) }
                    and { containsKeys("key3", "key4") }
                    and { not { containsKeys("key1", "key2") } }
                }
            }

            it("return empty map if all values are null") {
                //arrange
                val mapWithNullValues = mapOf(
                    "key1" to null,
                    "key2" to null,
                    "key3" to null,
                    "key4" to null
                )

                //act
                val result = mapWithNullValues.filterNotNullValues()

                //assert
                expectThat(result) {
                    and { isEmpty() }
                    and { not { containsKeys("key1", "key2", "key3", "key4") } }
                }
            }

            it("return empty map when map is empty") {
                //arrange
                val mapWithNullValues = emptyMap<String, String?>()

                //act
                val result = mapWithNullValues.filterNotNullValues()

                //assert
                expectThat(result) {
                    and { isEmpty() }
                }
            }

            it("return all values when map does not contain null values") {
                //arrange
                val mapWithNullValues = mapOf(
                    "key1" to "value1",
                    "key2" to "value2",
                    "key3" to "value3",
                    "key4" to "value4"
                )
                //act
                val result = mapWithNullValues.filterNotNullValues()

                //assert
                expectThat(result) {
                    and { hasSize(4) }
                    and { containsKeys("key1", "key2", "key3", "key4") }
                }
            }
        }
    }
}
