package com.psinder.plugins

import com.psinder.utils.mockKtorInstallFunction
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.server.testing.*
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
