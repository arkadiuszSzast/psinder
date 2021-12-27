package com.psinder.account.commands.handlers

import an.awesome.pipelinr.Command
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.psinder.account.commands.LoginAccountCommand
import com.psinder.account.commands.LoginAccountCommandResult
import com.psinder.config.JwtConfig
import com.psinder.shared.jwt.JwtToken
import mu.KotlinLogging
import java.util.Date

internal class LoginAccountHandler :
    Command.Handler<LoginAccountCommand, LoginAccountCommandResult> {

    override fun handle(command: LoginAccountCommand): LoginAccountCommandResult {
        val (username, password) = command.loginAccountRequest
        logger.debug { "Starting authentication process for user: [${username.value}]" }

        val token = JWT.create()
            .withAudience(JwtConfig.audience)
            .withIssuer(JwtConfig.issuer)
            .withClaim("username", username.value)
            .withExpiresAt(Date(System.currentTimeMillis() + OneDayInMillis))
            .sign(Algorithm.HMAC256(JwtConfig.secret))

        return LoginAccountCommandResult(JwtToken.createOrThrow(token))
    }

    companion object {
        private val logger = KotlinLogging.logger {}
        private const val OneDayInMillis = 86400000
    }
}
