package com.psinder.plugins

import com.psinder.config.TracingConfig
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.*
import io.ktor.metrics.micrometer.*
import io.ktor.server.testing.*
import io.ktor.util.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify


internal class MicrometerKtTest : DescribeSpec({
    describe("micrometer feature install") {

        it("should not install when tracing is disabled") {
            withTestApplication {
//                mockkStatic("io.ktor.application.ApplicationFeatureKt")
                mockkStatic(ApplicationFeature::class.qualifiedName!!)
                every { application.install(any<MicrometerMetrics.Feature>()) } answers { this.value }

                verify(exactly = 0) { application.install(any<MicrometerMetrics.Feature>()) }

            }


        }
    }
})