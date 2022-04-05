package com.psinder.plugins

import com.psinder.config.JwtAuthConfig
import com.psinder.test.utils.mockKtorInstallFunction
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.server.testing.withTestApplication
import io.mockk.verify

class SecurityKtTest : DescribeSpec({

    describe("authentication feature install") {

        it("should install") {
            withTestApplication {
                // arrange
                mockKtorInstallFunction()

                // act
                application.configureSecurity(JwtAuthConfig)

                // assert
                verify(exactly = 1) { application.install(eq(Authentication), any()) }
            }
        }
    }
})
