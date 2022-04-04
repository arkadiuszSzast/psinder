package com.psinder.events

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.util.UUID

class EventMetadataToCommandMetadataKtTest : DescribeSpec({

    describe("EventMetadataToCommandMetadata") {

        it("can map correctly") {
            val eventMetadata = EventMetadata(CorrelationId(UUID.randomUUID()), CausationId(UUID.randomUUID()))

            val result = eventMetadata.toCommandMetadata()

            expectThat(result) {
                get { correlationId }.isEqualTo(eventMetadata.correlationId?.correlationId)
                get { causationId }.isEqualTo(eventMetadata.causationId?.causationId)
            }
        }

        it("can map correctly when causationId is null") {
            val eventMetadata = EventMetadata(CorrelationId(UUID.randomUUID()), null)

            val result = eventMetadata.toCommandMetadata()

            expectThat(result) {
                get { correlationId }.isEqualTo(eventMetadata.correlationId?.correlationId)
                get { causationId }.isEqualTo(eventMetadata.causationId?.causationId)
            }
        }

        it("can map correctly when correlationId is null") {
            val eventMetadata = EventMetadata(null, CausationId(UUID.randomUUID()))

            val result = eventMetadata.toCommandMetadata()

            expectThat(result) {
                get { correlationId }.isEqualTo(eventMetadata.correlationId?.correlationId)
                get { causationId }.isEqualTo(eventMetadata.causationId?.causationId)
            }
        }

        it("can map correctly when correlationId and causationId are nulls") {
            val eventMetadata = EventMetadata(null, null)

            val result = eventMetadata.toCommandMetadata()

            expectThat(result) {
                get { correlationId }.isEqualTo(eventMetadata.correlationId?.correlationId)
                get { causationId }.isEqualTo(eventMetadata.causationId?.causationId)
            }
        }
    }
})
