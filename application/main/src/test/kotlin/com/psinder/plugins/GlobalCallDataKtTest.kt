package com.psinder.plugins

import com.github.maaxgr.ktor.globalcalldata.GlobalCallData
import com.psinder.test.utils.mockKtorInstallFunction
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.install
import io.ktor.server.testing.withTestApplication
import io.mockk.verify

internal class GlobalCallDataKtTest : DescribeSpec({

    describe("Global call data feature install") {

        it("should install") {
            withTestApplication {
                mockKtorInstallFunction()

                application.configureGlobalCallData()

                verify(exactly = 1) { application.install(GlobalCallData) }
            }
        }
    }
})
