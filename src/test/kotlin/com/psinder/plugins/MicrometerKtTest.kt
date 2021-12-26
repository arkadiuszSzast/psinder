package com.psinder.plugins

import com.psinder.config.TracingConfig
import com.psinder.utils.mockKtorInstallFunction
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.*
import io.ktor.metrics.micrometer.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify

internal class MicrometerKtTest : DescribeSpec({

    describe("micrometer feature install") {

        it("should not install when tracing is disabled") {
            withTestApplication {
                mockKtorInstallFunction()
                mockkObject(TracingConfig)
                every { TracingConfig.enabled } returns false

                application.configureMicrometer(TracingConfig)

                verify(exactly = 0) { application.install(eq(MicrometerMetrics), any()) }
            }
        }

        it("should install when tracing is enabled") {
            withTestApplication {
                mockKtorInstallFunction()
                mockkObject(TracingConfig)
                every { TracingConfig.enabled } returns true

                application.configureMicrometer(TracingConfig)

                verify(exactly = 1) { application.install(eq(MicrometerMetrics), any()) }
            }
        }
    }
})
