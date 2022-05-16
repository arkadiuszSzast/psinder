package com.psinder.account.commands

import arrow.core.getOrElse
import arrow.core.toOption
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.psinder.account.AccountAggregate
import com.psinder.account.AccountDto
import com.psinder.account.AccountRepository
import com.psinder.account.LogInFailureError
import com.psinder.account.events.AccountLoggedInFailureEvent
import com.psinder.account.events.AccountLoggedInSuccessEvent
import com.psinder.account.logIn
import com.psinder.auth.role.Role
import com.psinder.config.JwtAuthConfig
import com.psinder.events.appendToStream
import com.psinder.shared.EmailAddress
import com.psinder.shared.jwt.JwtToken
import com.trendyol.kediatr.AsyncCommandWithResultHandler
import io.traxter.eventstoredb.EventStoreDB
import mu.KotlinLogging
import org.litote.kmongo.Id
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.codifiedEnum
import java.util.Date

internal class LoginAccountHandler(
    private val jwtConfig: JwtAuthConfig,
    private val accountRepository: AccountRepository,
    private val eventStore: EventStoreDB,
) : AsyncCommandWithResultHandler<LoginAccountCommand, LoginAccountCommandResult> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(command: LoginAccountCommand): LoginAccountCommandResult {
        val (email, password) = command.loginAccountRequest
        logger.debug { "Starting authentication process for user: [$email]" }

        return accountRepository.findOneByEmail(email).toOption()
            .map {
                when (val event = AccountAggregate.Events.logIn(it.id.cast(), it.status, it.password, password)) {
                    is AccountLoggedInFailureEvent -> {
                        logger.warn { "Authenticating $email failed. Error code: ${event.error}" }
                        eventStore.appendToStream(event, command.metadata)
                        LoginAccountCommandFailure(email, event.error)
                    }
                    is AccountLoggedInSuccessEvent -> {
                        logger.debug { "Authenticating $email succeeded" }
                        eventStore.appendToStream(event, command.metadata)
                        LoginAccountCommandSucceed(email, getToken(it.id.cast(), it.email, it.role))
                    }
                }
            }
            .getOrElse {
                logger.warn { "Authenticating $email failed. No account with given email found" }
                LoginAccountCommandFailure(email, LogInFailureError.AccountNotFound.codifiedEnum())
            }
    }

    private fun getToken(accountId: Id<AccountDto>, email: EmailAddress, role: CodifiedEnum<Role, String>) =
        JWT.create()
            .withAudience(jwtConfig.audience)
            .withIssuer(jwtConfig.issuer)
            .withClaim("accountId", accountId.toString())
            .withClaim("email", email.value)
            .withClaim("role", role.code())
            .withExpiresAt(Date(System.currentTimeMillis() + jwtConfig.expirationInMillis))
            .sign(Algorithm.HMAC256(jwtConfig.secret))
            .let { JwtToken.createOrThrow(it) }
}
