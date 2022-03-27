package com.psinder.mail

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class SendGridConfigTest : DescribeSpec({

    describe("get sendgrid config") {

        it("get properties") {
            expectThat(SendGridConfig) {
                get { apiKey }.isEqualTo("sendgrid_api_key")
            }
        }
    }
})
