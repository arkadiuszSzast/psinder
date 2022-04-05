package com.psinder.account.activation

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.psinder.shared.jwt.JwtToken
import com.psinder.shared.validation.ValidationError
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.arrow.isLeft
import strikt.arrow.isRight
import strikt.assertions.containsExactly
import strikt.assertions.isTrue

class ActivationTokenTest : DescribeSpec() {

    init {

        describe("ActivationToken") {

            it("can mark as used") {
                // arrange
                val jwt = JWT.create().sign(Algorithm.HMAC256("secret"))
                val activateToken = ActivationToken(JwtToken.createOrThrow(jwt))

                // act
                val result = ActivationToken.use(activateToken)

                // assert
                expectThat(result)
                    .isRight()
                    .get { value }
                    .get { used }.isTrue()
            }

            it("return left when token is already used") {
                // arrange
                val jwt = JWT.create().sign(Algorithm.HMAC256("secret"))
                val activateToken = ActivationToken(JwtToken.createOrThrow(jwt), used = true)

                // act
                val result = ActivationToken.use(activateToken)

                // assert
                expectThat(result)
                    .isLeft()
                    .get { value }
                    .get { validationErrors }.containsExactly(
                        ValidationError(
                            ".used",
                            "validation.account_activate_token_already_used"
                        )
                    )
            }
        }
    }
}
