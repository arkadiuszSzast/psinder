package com.psinder.plugins

import com.psinder.utils.mockKtorInstallFunction
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.*
import io.ktor.server.testing.*
import io.mockk.verify
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.getKoin
import org.koin.test.check.checkModules

class KoinKtTest : DescribeSpec({

    describe("Koin feature install") {

        it("should install") {
            withTestApplication {
                mockKtorInstallFunction()

                application.configureKoin()

                verify(exactly = 1) { application.install(eq(Koin), any()) }
            }
        }
    }

    describe("check installed modules") {

        it("verify all modules") {
            withTestApplication {
                application.configureKoin()
                application.getKoin().checkModules()
            }
        }
    }

})
