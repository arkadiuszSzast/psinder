package com.psinder.shared.jwt

import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.psinder.test.utils.jwt
import com.psinder.test.utils.jwtToken
import io.kotest.core.spec.style.DescribeSpec
import pl.brightinventions.codified.enums.codifiedEnum
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.arrow.isInvalid
import strikt.arrow.isValid
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isFailure
import strikt.assertions.isSuccess
import java.time.Instant

class JwtTokenTest : DescribeSpec() {

    init {

        describe("JwtToken") {

            it("can create with valid token using create method") {
                // arrange
                val validJwt = jwt { secret = "secret" }

                // act
                val result = JwtToken.create(validJwt)

                // assert
                expectThat(result)
                    .isValid()
            }

            it("can create with valid token using createOrThrow method") {
                // arrange
                val validJwt = jwt { secret = "secret" }

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

            it("get subject") {
                // arrange
                val validJwt = jwt {
                    subject = "account-123"
                    secret = "secret"
                }

                // act
                val result = JwtToken.createOrThrow(validJwt)

                // assert
                expectThat(result)
                    .get { getSubjectUnsafe() }.isEqualTo("account-123")
            }

            describe("verify") {

                it("AlgorithmMismatch") {
                    // arrange
                    val jwt = jwtToken { secret = "secret" }

                    // act
                    val result = jwt.verify(Algorithm.HMAC384("secret"))

                    // assert
                    expectThat(result)
                        .isInvalid()
                        .get { value }.isEqualTo(JwtValidationError.AlgorithmMismatch.codifiedEnum())
                }

                it("InvalidSignature") {
                    // arrange
                    val jwt = jwtToken { secret = "secret" }

                    // act
                    val result = jwt.verify(Algorithm.HMAC256("invalid"))

                    // assert
                    expectThat(result)
                        .isInvalid()
                        .get { value }.isEqualTo(JwtValidationError.InvalidSignature.codifiedEnum())
                }

                it("Expired") {
                    // arrange
                    val jwt = jwtToken { expirationDate = Instant.now().minusSeconds(10) }

                    // act
                    val result = jwt.verify(Algorithm.HMAC256("secret"))

                    // assert
                    expectThat(result)
                        .isInvalid()
                        .get { value }.isEqualTo(JwtValidationError.Expired.codifiedEnum())
                }
            }
        }
    }
}
