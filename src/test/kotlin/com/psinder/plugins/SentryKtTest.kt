package com.psinder.plugins

import com.psinder.config.SentryConfig
import com.psinder.features.SentryFeature
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify

class SentryKtTest : DescribeSpec({

    describe("sentry feature install") {

        it("should not install when disabled") {
            withTestApplication {
                mockkStatic("io.ktor.application.ApplicationFeatureKt")
                mockkObject(SentryConfig)
                every { SentryConfig.enabled } returns false
                every { SentryConfig.dsn} returns "https://public@sentry.example.com/1"

                application.initializeSentry(SentryConfig)

                verify(exactly = 0) { application.install(eq(SentryFeature), any()) }
            }
        }

        it("should install when enabled") {
            withTestApplication {
                mockkStatic("io.ktor.application.ApplicationFeatureKt")
                mockkObject(SentryConfig)
                every { SentryConfig.enabled } returns true
                every { SentryConfig.dsn } returns "https://public@sentry.example.com/1"

                application.initializeSentry(SentryConfig)

                verify(exactly = 1) { application.install(eq(SentryFeature), any()) }
            }
        }
    }

})
