package com.psinder.events.plugins

import com.psinder.events.config.EventStoreConfig
import com.psinder.test.utils.mockKtorInstallFunction
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.application.install
import io.ktor.server.testing.withTestApplication
import io.mockk.verify
import io.traxter.eventstoredb.EventStoreDB

class EventStoreKtTest : DescribeSpec({

    describe("event store feature install") {

        it("should install") {
            withTestApplication {
                // arrange
                mockKtorInstallFunction()

                // act
                application.configureEventStore(EventStoreConfig)

                // assert
                verify(exactly = 1) { application.install(eq(EventStoreDB), any()) }
            }
        }
    }
})
