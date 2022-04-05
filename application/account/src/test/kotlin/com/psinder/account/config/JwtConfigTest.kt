package com.psinder.account.config

import com.psinder.auth.JwtExpirationTime
import com.psinder.auth.JwtIssuer
import com.psinder.auth.JwtProperties
import com.psinder.auth.JwtSecret
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class JwtConfigTest : DescribeSpec({

    describe("get jwt config test") {

        it("get properties") {
            // arrange && act && assert
            expectThat(JwtConfig) {
                get { activateAccount }.isEqualTo(
                    JwtProperties(
                        JwtSecret("activate_jwt_test_secret"),
                        JwtIssuer("psinder_test"),
                        JwtExpirationTime(4320000)
                    )
                )
            }
        }
    }
})
