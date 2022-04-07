package com.psinder.shared.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.arrow.isInvalid
import strikt.arrow.isValid
import strikt.assertions.isA
import strikt.assertions.isFailure
import strikt.assertions.isSuccess

class JwtTokenTest : DescribeSpec() {

    init {

        describe("JwtToken") {

            it("can create with valid token using create method") {
                // arrange
                val validJwt = JWT.create().sign(Algorithm.HMAC256("secret"))

                // act
                val result = JwtToken.create(validJwt)

                // assert
                expectThat(result)
                    .isValid()
            }

            it("can create with valid token using createOrThrow method") {
                // arrange
                val validJwt = JWT.create().sign(Algorithm.HMAC256("secret"))

                // act && assert
                expectCatching { JwtToken.createOrThrow(validJwt) }.isSuccess()
            }

            it("returns invalid") {
                // arrange
                val invalidJwt = "abc"

                // act
                val result = JwtToken.create(invalidJwt)

                // assert
                expectThat(result)
                    .isInvalid()
            }

            it("throws exception") {
                // arrange
                val invalidJwt = "abc"

                // act && assert
                expectCatching { JwtToken.createOrThrow(invalidJwt) }.isFailure().isA<JWTDecodeException>()
            }
        }
    }
}
