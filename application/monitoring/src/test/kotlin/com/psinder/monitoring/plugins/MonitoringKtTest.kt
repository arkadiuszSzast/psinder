package com.psinder.monitoring.plugins

import com.psinder.test.utils.mockKtorInstallFunction
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.server.testing.withTestApplication
import io.mockk.verify

class MonitoringKtTest : DescribeSpec({

    describe("CallLogging feature install") {

        it("should install") {
            withTestApplication {
                mockKtorInstallFunction()

                application.configureMonitoring()

                verify(exactly = 1) { application.install(eq(CallLogging), any()) }
            }
        }
    }
})