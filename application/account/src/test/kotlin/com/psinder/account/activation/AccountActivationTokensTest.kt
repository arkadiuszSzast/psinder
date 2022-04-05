package com.psinder.account.activation

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.psinder.auth.account.AccountId
import com.psinder.shared.jwt.JwtToken
import io.kotest.core.spec.style.DescribeSpec
import org.litote.kmongo.newId
import strikt.api.expectThat
import strikt.assertions.containsExactly

class AccountActivationTokensTest : DescribeSpec() {

    init {

        describe("AccountActivationTokens") {

            it("can append token when current list is empty") {
                // arrange
                val accountActivationTokens = AccountActivationTokens(newId(), AccountId("123"), emptyList())
                val jwt = JWT.create().sign(Algorithm.HMAC256("secret"))
                val activateToken = ActivationToken(JwtToken.createOrThrow(jwt))

                // act
                val result = AccountActivationTokens.appendToken(accountActivationTokens, activateToken)

                // assert
                expectThat(result)
                    .get { tokens }
                    .containsExactly(activateToken)
            }

            it("can append token when already contains some tokens ") {
                // arrange
                val existingToken =
                    ActivationToken(JwtToken.createOrThrow(JWT.create().sign(Algorithm.HMAC256("secret"))))
                val accountActivationTokens = AccountActivationTokens(newId(), AccountId("123"), listOf(existingToken))
                val newToken = ActivationToken(JwtToken.createOrThrow(JWT.create().sign(Algorithm.HMAC256("secret"))))

                // act
                val result = AccountActivationTokens.appendToken(accountActivationTokens, newToken)

                // assert
                expectThat(result)
                    .get { tokens }
                    .containsExactly(existingToken, newToken)
            }
        }
    }
}
