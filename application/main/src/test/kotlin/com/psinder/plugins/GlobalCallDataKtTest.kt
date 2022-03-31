package com.psinder.plugins

import com.psinder.test.utils.mockKtorInstallFunction
import com.szastarek.ktor.globalrequestdata.GlobalRequestData
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.install
import io.ktor.server.testing.withTestApplication
import io.mockk.verify

internal class GlobalCallDataKtTest : DescribeSpec({

    describe("Global call data feature install") {

        it("should install") {
            withTestApplication {
                mockKtorInstallFunction()

                application.configureGlobalRequestData()

                verify(exactly = 1) { application.install(GlobalRequestData) }
            }
        }
    }
})
