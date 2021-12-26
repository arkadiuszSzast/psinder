package com.psinder.config

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class TracingConfigTest : DescribeSpec({

    describe("get tracing config") {

        it("get properties") {
            expectThat(TracingConfig) {
                get { enabled }.isEqualTo(false)
            }
        }
    }
})
