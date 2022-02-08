package com.psinder.account.commands

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.psinder.config.JwtConfig
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
            .withAudience(JwtConfig.audience)
            .withIssuer(JwtConfig.issuer)
            .withClaim("username", username.value)
            .withExpiresAt(Date(System.currentTimeMillis() + OneDayInMillis))
            .sign(Algorithm.HMAC256(JwtConfig.secret))

        return LoginAccountCommandResult(JwtToken.createOrThrow(""))
    }

    companion object {
        private const val OneDayInMillis = 86400000
    }
}
