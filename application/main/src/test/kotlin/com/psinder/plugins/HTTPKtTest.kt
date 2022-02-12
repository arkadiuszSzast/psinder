package com.psinder.plugins

import com.psinder.test.utils.mockKtorInstallFunction
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.PartialContent
import io.ktor.server.testing.withTestApplication
import io.mockk.verify

class HTTPKtTest : DescribeSpec({

    describe("CORS and PartialContent feature install") {

        it("should install") {
            withTestApplication {
                mockKtorInstallFunction()

                application.configureHTTP()

                verify(exactly = 1) { application.install(eq(CORS), any()) }
                verify(exactly = 1) { application.install(eq(PartialContent), any()) }
            }
        }
    }
})
