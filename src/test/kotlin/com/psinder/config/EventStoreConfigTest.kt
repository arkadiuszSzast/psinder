package com.psinder.config

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class EventStoreConfigTest : DescribeSpec({

    describe("get event store config") {

        it("get properties") {
            expectThat(EventStoreConfig) {
                get { connectionString }.isEqualTo("event-store-connection-string")
            }
        }
    }

})
