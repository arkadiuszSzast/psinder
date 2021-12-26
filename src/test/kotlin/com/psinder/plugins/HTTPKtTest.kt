package com.psinder.plugins

import com.psinder.utils.mockKtorInstallFunction
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.server.testing.*
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
