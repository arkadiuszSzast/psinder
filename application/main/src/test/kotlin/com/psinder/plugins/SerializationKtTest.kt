package com.psinder.plugins

import com.psinder.test.utils.mockKtorInstallFunction
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.server.testing.withTestApplication
import io.mockk.verify
import kotlinx.serialization.json.Json

class SerializationKtTest : DescribeSpec({

    describe("ContentNegotiation feature install") {

        it("should install") {
            withTestApplication {
                mockKtorInstallFunction()
                application.configureSerialization(Json { })

                verify(exactly = 1) { application.install(eq(ContentNegotiation), any()) }
            }
        }
    }
})
