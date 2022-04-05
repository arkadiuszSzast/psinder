package com.psinder.plugins

import com.psinder.test.utils.mockKtorInstallFunction
import com.psinder.utils.allModules
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.server.testing.withTestApplication
import io.mockk.verify
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.getKoin
import org.koin.test.check.checkModules

class KoinKtTest : DescribeSpec({

    describe("Koin feature install") {

        it("should install") {
            withTestApplication {
                // arrange
                mockKtorInstallFunction()

                // act
                application.configureKoin()

                // assert
                verify(exactly = 1) { application.install(eq(Koin), any()) }
            }
        }
    }

    describe("check installed modules") {

        it("verify all modules") {
            withTestApplication(Application::allModules) {
                // arrange && act && assert
                application.getKoin().checkModules()
            }
        }
    }
})
