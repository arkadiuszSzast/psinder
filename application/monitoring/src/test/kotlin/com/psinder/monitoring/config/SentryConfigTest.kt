package com.psinder.monitoring.config

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class SentryConfigTest : DescribeSpec({

    describe("get sentry config") {

        it("get properties") {
            // arrange && act && assert
            expectThat(SentryConfig) {
                get { enabled }.isEqualTo(false)
                get { dsn }.isEqualTo("sentry-dsn")
            }
        }
    }
})
