package com.psinder.monitoring.plugins

import com.psinder.monitoring.config.TracingConfig
import com.psinder.test.utils.mockKtorInstallFunction
import com.zopa.ktor.opentracing.OpenTracingServer
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.install
import io.ktor.server.testing.withTestApplication
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify

class OpentracingKtTest : DescribeSpec({

    describe("opentracing feature install") {

        it("should not install when tracing is disabled") {
            withTestApplication {
                mockKtorInstallFunction()
                mockkObject(TracingConfig)
                every { TracingConfig.enabled } returns false

                application.configureOpentracing(TracingConfig)

                verify(exactly = 0) { application.install(eq(OpenTracingServer), any()) }
            }
        }

        it("should install when tracing is enabled") {
            withTestApplication {
                mockKtorInstallFunction()
                mockkObject(TracingConfig)
                every { TracingConfig.enabled } returns true

                application.configureOpentracing(TracingConfig)

                verify(exactly = 1) { application.install(eq(OpenTracingServer), any()) }
            }
        }
    }
})