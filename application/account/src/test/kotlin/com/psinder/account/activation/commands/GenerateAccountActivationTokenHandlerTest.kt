package com.psinder.account.activation.commands

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.psinder.account.AccountDto
import com.psinder.account.config.JwtConfig
import com.psinder.auth.principal.CanDoAnythingAbilityProvider
import com.psinder.database.RecordingEventStoreDB
import io.kotest.core.spec.style.DescribeSpec
import org.litote.kmongo.newId
import strikt.api.expectCatching
import strikt.assertions.isSuccess

class GenerateAccountActivationTokenHandlerTest : DescribeSpec() {

    private val eventStore = RecordingEventStoreDB()
    private val acl = CanDoAnythingAbilityProvider()
    private val handler = GenerateAccountActivationTokenHandler(JwtConfig, eventStore, acl)

    init {
        describe("GenerateAccountActivateTokenHandler") {

            it("should generate valid token") {
                // arrange
                val accountId = newId<AccountDto>()

                // act
                val result = handler.handleAsync(GenerateAccountActivationTokenCommand(accountId))

                // assert
                expectCatching {
                    JWT.require(Algorithm.HMAC256(JwtConfig.activateAccount.secret.value))
                        .withSubject(accountId.toString())
                        .withIssuer(JwtConfig.activateAccount.issuer.value)
                        .build()
                        .verify(result.token.token)
                }.isSuccess()
            }
        }
    }
}
