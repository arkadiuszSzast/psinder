package com.psinder.account.activation.commands

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.psinder.account.activation.events.AccountActivationTokenGeneratedEvent
import com.psinder.account.config.JwtConfig
import com.psinder.auth.authority.generateAccountActivateTokenFeature
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.events.streamName
import com.psinder.events.toEventData
import com.psinder.shared.jwt.JwtToken
import com.trendyol.kediatr.AsyncCommandWithResultHandler
import io.traxter.eventstoredb.EventStoreDB
import mu.KotlinLogging
import java.util.Date

internal class GenerateAccountActivateTokenHandler(
    private val jwtConfig: JwtConfig,
    private val eventStore: EventStoreDB,
    private val acl: AuthorizedAccountAbilityProvider
) : AsyncCommandWithResultHandler<GenerateAccountActivateTokenCommand, GenerateAccountActivateTokenCommandResult> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(command: GenerateAccountActivateTokenCommand): GenerateAccountActivateTokenCommandResult {
        acl.ensure().hasAccessTo(generateAccountActivateTokenFeature)
        val (accountId, metadata) = command
        val (secret, issuer, expirationTime) = jwtConfig.activateAccount

        logger.debug { "Generating account activation token for accountId: $accountId" }

        val token = JWT.create()
            .withIssuer(issuer.value)
            .withSubject(accountId.toString())
            .withExpiresAt(Date(System.currentTimeMillis() + expirationTime.millis))
            .sign(Algorithm.HMAC256(secret.value))
            .let { JwtToken.createOrThrow(it) }

        val event = AccountActivationTokenGeneratedEvent(accountId, token)
        eventStore.appendToStream(
            event.streamName,
            event.toEventData<AccountActivationTokenGeneratedEvent>(metadata)
        )

        return GenerateAccountActivateTokenCommandResult(token)
    }
}
