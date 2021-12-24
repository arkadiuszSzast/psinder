package com.psinder.config

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class ApplicationConfigTest : DescribeSpec({

    describe("get application config") {

        it("get properties") {
            expectThat(ApplicationConfig) {
                get { environment }.isEqualTo("dev")
            }
        }
    }
})