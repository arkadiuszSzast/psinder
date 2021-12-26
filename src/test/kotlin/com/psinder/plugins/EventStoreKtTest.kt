package com.psinder.plugins

import com.psinder.config.EventStoreConfig
import com.psinder.utils.mockKtorInstallFunction
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.install
import io.ktor.server.testing.withTestApplication
import io.mockk.verify
import io.traxter.eventstoredb.EventStoreDB

class EventStoreKtTest : DescribeSpec({

    describe("event store feature install") {

        it("should install") {
            withTestApplication {
                mockKtorInstallFunction()

                application.configureEventStore(EventStoreConfig)

                verify(exactly = 1) { application.install(eq(EventStoreDB), any()) }
            }
        }
    }
})
