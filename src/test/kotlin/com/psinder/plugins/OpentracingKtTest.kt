package com.psinder.plugins

import com.psinder.config.TracingConfig
import com.zopa.ktor.opentracing.OpenTracingServer
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify

class OpentracingKtTest : DescribeSpec({

    describe("opentracing feature install") {

        it("should not install when tracing is disabled") {
            withTestApplication {
                mockkStatic("io.ktor.application.ApplicationFeatureKt")
                mockkObject(TracingConfig)
                every { TracingConfig.enabled } returns false

                application.configureOpentracing(TracingConfig)

                verify(exactly = 0) { application.install(eq(OpenTracingServer), any()) }
            }
        }

        it("should install when tracing is enabled") {
            withTestApplication {
                mockkStatic("io.ktor.application.ApplicationFeatureKt")
                mockkObject(TracingConfig)
                every { TracingConfig.enabled } returns true

                application.configureOpentracing(TracingConfig)

                verify(exactly = 1) { application.install(eq(OpenTracingServer), any()) }
            }
        }
    }

})
