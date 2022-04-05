package com.psinder.account.commands

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.psinder.config.JwtAuthConfig
import com.psinder.shared.jwt.JwtToken
import com.trendyol.kediatr.AsyncCommandWithResultHandler
import mu.KotlinLogging
import java.util.Date

internal class LoginAccountHandler : AsyncCommandWithResultHandler<LoginAccountCommand, LoginAccountCommandResult> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(command: LoginAccountCommand): LoginAccountCommandResult {
        val (username, password) = command.loginAccountRequest
        logger.debug { "Starting authentication process for user: [${username.value}]" }

        val token = JWT.create()
            .withAudience(JwtAuthConfig.audience)
            .withIssuer(JwtAuthConfig.issuer)
            .withClaim("username", username.value)
            .withClaim("accountId", "123")
            .withClaim("email", "joe@doe.com")
            .withClaim("role", "Admin")
            .withExpiresAt(Date(System.currentTimeMillis() + OneDayInMillis))
            .sign(Algorithm.HMAC256(JwtAuthConfig.secret))

        return LoginAccountCommandResult(JwtToken.createOrThrow(token))
    }

    companion object {
        private const val OneDayInMillis = 86400000
    }
}
