package com.psinder.aws.config

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class AWSConfigTest : DescribeSpec() {

    init {

        describe("get aws config") {

            it("get properties") {
                // arrange && act && assert
                expectThat(AWSConfig) {
                    get { useLocalstack }.isEqualTo(true)
                }
            }
        }
    }
}
