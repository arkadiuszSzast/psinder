package com.psinder.monitoring.plugins

import com.psinder.monitoring.config.TracingConfig
import com.psinder.test.utils.mockKtorInstallFunction
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.install
import io.ktor.metrics.micrometer.MicrometerMetrics
import io.ktor.server.testing.withTestApplication
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify

internal class MicrometerKtTest : DescribeSpec({

    describe("micrometer feature install") {

        it("should not install when tracing is disabled") {
            withTestApplication {
                // arrange
                mockKtorInstallFunction()
                mockkObject(TracingConfig)
                every { TracingConfig.enabled } returns false

                // act
                application.configureMicrometer(TracingConfig)

                // assert
                verify(exactly = 0) { application.install(eq(MicrometerMetrics), any()) }
            }
        }

        it("should install when tracing is enabled") {
            withTestApplication {
                // arrange
                mockKtorInstallFunction()
                mockkObject(TracingConfig)
                every { TracingConfig.enabled } returns true

                // act
                application.configureMicrometer(TracingConfig)

                // assert
                verify(exactly = 1) { application.install(eq(MicrometerMetrics), any()) }
            }
        }
    }
})
