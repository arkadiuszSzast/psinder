package com.psinder.config

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class JwtConfigTest : DescribeSpec({

    describe("get jwt config") {

        it("get properties") {
            expectThat(JwtConfig) {
                get { domain }.isEqualTo("https://jwt-test-domain/")
                get { audience }.isEqualTo("jwt-test-audience")
                get { realm }.isEqualTo("jwt-test-realm")
                get { issuer }.isEqualTo("jwt-test-issuer")
                get { secret }.isEqualTo("test-secret")
            }
        }
    }
})