package com.psinder.account.commands.handlers

import com.psinder.account.commands.LoginAccountCommand
import com.psinder.account.commands.LoginAccountCommandResult
import com.psinder.shared.jwt.JwtToken
import com.trendyol.kediatr.AsyncCommandWithResultHandler
import mu.KotlinLogging

internal class LoginAccountHandler : AsyncCommandWithResultHandler<LoginAccountCommand, LoginAccountCommandResult> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(command: LoginAccountCommand): LoginAccountCommandResult {
        val (username, password) = command.loginAccountRequest
        logger.debug { "Starting authentication process for user: [${username.value}]" }

        // val token = JWT.create()
        //     .withAudience(JwtConfig.audience)
        //     .withIssuer(JwtConfig.issuer)
        //     .withClaim("username", username.value)
        //     .withExpiresAt(Date(System.currentTimeMillis() + OneDayInMillis))
        //     .sign(Algorithm.HMAC256(JwtConfig.secret))

        return LoginAccountCommandResult(JwtToken.createOrThrow(""))
    }

    companion object {
        private const val OneDayInMillis = 86400000
    }
}
