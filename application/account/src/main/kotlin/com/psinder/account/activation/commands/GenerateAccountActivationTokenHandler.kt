package com.psinder.account.activation.commands

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.psinder.account.config.JwtConfig
import com.psinder.auth.authority.generateAccountActivationTokenFeature
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.shared.jwt.JwtToken
import com.trendyol.kediatr.AsyncCommandWithResultHandler
import mu.KotlinLogging
import java.util.Date

internal class GenerateAccountActivationTokenHandler(
    private val jwtConfig: JwtConfig,
    private val acl: AuthorizedAccountAbilityProvider
) : AsyncCommandWithResultHandler<GenerateAccountActivationTokenCommand, GenerateAccountActivationTokenCommandResult> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(command: GenerateAccountActivationTokenCommand): GenerateAccountActivationTokenCommandResult {
        acl.ensure().hasAccessTo(generateAccountActivationTokenFeature)
        val (accountId, metadata) = command
        val (secret, issuer, expirationTime) = jwtConfig.activateAccount
        logger.debug { "Generating account activation token for accountId: $accountId" }

        val token = JWT.create()
            .withIssuer(issuer.value)
            .withSubject(accountId.toString())
            .withExpiresAt(Date(System.currentTimeMillis() + expirationTime.millis))
            .sign(Algorithm.HMAC256(secret.value))
            .let { JwtToken.createOrThrow(it) }

        return GenerateAccountActivationTokenCommandResult(token)
    }
}
