package com.psinder.monitoring.config

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class TracingConfigTest : DescribeSpec({

    describe("get tracing config") {

        it("get properties") {
            // arrange && act && assert
            expectThat(TracingConfig) {
                get { enabled }.isEqualTo(false)
            }
        }
    }
})
