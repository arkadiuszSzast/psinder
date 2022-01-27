package com.psinder.plugins

import com.psinder.shared.json.JsonMapper
import com.psinder.utils.mockKtorInstallFunction
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.server.testing.withTestApplication
import io.mockk.verify

class SerializationKtTest : DescribeSpec({

    describe("ContentNegotiation feature install") {

        it("should install") {
            withTestApplication {
                mockKtorInstallFunction()
                application.configureSerialization(JsonMapper.defaultMapper)

                verify(exactly = 1) { application.install(eq(ContentNegotiation), any()) }
            }
        }
    }
})
