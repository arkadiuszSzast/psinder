package com.psinder.plugins

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.psinder.utils.mockKtorInstallFunction
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.server.testing.*
import io.mockk.verify

class SerializationKtTest : DescribeSpec({

    describe("ContentNegotiation feature install") {

        it("should install") {
            withTestApplication {
                mockKtorInstallFunction()
                application.configureSerialization(jacksonObjectMapper())

                verify(exactly = 1) { application.install(eq(ContentNegotiation), any()) }
            }
        }
    }
})
