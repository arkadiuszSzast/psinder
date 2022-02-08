package com.psinder.monitoring.plugins

import com.psinder.monitoring.config.SentryConfig
import com.psinder.monitoring.features.SentryFeature
import com.psinder.test.utils.mockKtorInstallFunction
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.install
import io.ktor.server.testing.withTestApplication
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify

class SentryKtTest : DescribeSpec({

    describe("sentry feature install") {

        it("should not install when disabled") {
            withTestApplication {
                mockKtorInstallFunction()
                mockkObject(SentryConfig)
                every { SentryConfig.enabled } returns false
                every { SentryConfig.dsn } returns "https://public@sentry.example.com/1"

                application.initializeSentry(SentryConfig)

                verify(exactly = 0) { application.install(eq(SentryFeature), any()) }
            }
        }

        it("should install when enabled") {
            withTestApplication {
                mockKtorInstallFunction()
                mockkObject(SentryConfig)
                every { SentryConfig.enabled } returns true
                every { SentryConfig.dsn } returns "https://public@sentry.example.com/1"

                application.initializeSentry(SentryConfig)

                verify(exactly = 1) { application.install(eq(SentryFeature), any()) }
            }
        }
    }
})