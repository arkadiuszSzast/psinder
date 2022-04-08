package com.psinder.account.activation.commands

import arrow.core.Validated
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.psinder.account.Account
import com.psinder.account.AccountDto
import com.psinder.account.activate
import com.psinder.account.activation.events.AccountActivatedEvent
import com.psinder.account.activation.events.AccountActivationEvent
import com.psinder.account.activation.events.AccountActivationFailureEvent
import com.psinder.account.config.JwtConfig
import com.psinder.account.queries.FindAccountByIdQuery
import com.psinder.auth.authority.activatingAccountFeature
import com.psinder.auth.authority.authorities
import com.psinder.auth.authority.withInjectedAuthoritiesReturning
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.events.appendToStream
import com.psinder.shared.jwt.JwtValidationError
import com.psinder.shared.jwt.TokenMissingSubjectException
import com.trendyol.kediatr.AsyncCommandWithResultHandler
import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.CommandMetadata
import io.traxter.eventstoredb.EventStoreDB
import mu.KotlinLogging
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.id.toId
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.codifiedEnum

internal class ActivateAccountCommandHandler(
    private val jwtConfig: JwtConfig,
    private val eventStore: EventStoreDB,
    private val commandBus: CommandBus,
    private val acl: AuthorizedAccountAbilityProvider
) : AsyncCommandWithResultHandler<ActivateAccountCommand, ActivateAccountCommandResult> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(command: ActivateAccountCommand): ActivateAccountCommandResult {
        val activateAccountJwtProperties = jwtConfig.activateAccount
        val (token, metadata) = command
        val subjectUnsafe = token.getSubjectUnsafe() ?: throw TokenMissingSubjectException()
        val subjectAsAccountId = ObjectId(subjectUnsafe).toId<AccountDto>()
        logger.debug { "Trying to activate account with id: $subjectAsAccountId" }

        acl.hasAccessTo(activatingAccountFeature).onDeny {
            val error = AccountActivationError.MissingPermissions.codifiedEnum()
            return handleActivationFailed(subjectAsAccountId, error, metadata)
        }

        return when (val decodedJwt = token.verify(Algorithm.HMAC256(activateAccountJwtProperties.secret.value))) {
            is Validated.Invalid -> handleInvalidJwt(subjectAsAccountId, decodedJwt.value, metadata)
            is Validated.Valid -> tryActivateAccount(decodedJwt.value, metadata)
        }
    }

    private suspend fun tryActivateAccount(
        token: DecodedJWT,
        commandMetadata: CommandMetadata?
    ): ActivateAccountCommandResult {
        val accountId = token.subject?.let { ObjectId(it) }?.toId<AccountDto>() ?: throw TokenMissingSubjectException()
        val authoritiesToInject = authorities {
            entityAccess(Account::class) {
                viewScope { account, _ -> account.id == accountId.cast<Account>() }
            }
        }
        val account = withInjectedAuthoritiesReturning(authoritiesToInject) {
            commandBus.executeQueryAsync(FindAccountByIdQuery(accountId)).account.orNull()
        }

        return when (account) {
            null -> handleActivationFailed(
                accountId,
                AccountActivationError.AccountNotFound.codifiedEnum(),
                commandMetadata
            )
            else -> {
                val event = Account.activate(accountId.cast(), account.status)
                eventStore.appendToStream(event, commandMetadata)
                event.toCommandResult()
            }
        }
    }

    private suspend fun handleInvalidJwt(
        accountId: Id<AccountDto>,
        error: CodifiedEnum<JwtValidationError, String>,
        commandMetadata: CommandMetadata?
    ): ActivateAccountCommandResult {
        val reason = when (error.knownOrNull()) {
            JwtValidationError.Expired -> AccountActivationError.TokenExpired
            else -> AccountActivationError.TokenInvalid
        }
        return handleActivationFailed(accountId, reason.codifiedEnum(), commandMetadata)
    }

    private suspend fun handleActivationFailed(
        accountId: Id<AccountDto>,
        reason: CodifiedEnum<AccountActivationError, String>,
        commandMetadata: CommandMetadata?
    ): ActivateAccountCommandResult {
        val event = AccountActivationFailureEvent(accountId, reason)
        eventStore.appendToStream(event, commandMetadata)
        logger.debug { "Account with id $accountId has not been activated because of $reason" }
        return event.toCommandResult()
    }

    private fun AccountActivationEvent.toCommandResult() = when (this) {
        is AccountActivatedEvent -> ActivateAccountCommandSucceed(this.accountId)
        is AccountActivationFailureEvent -> ActivateAccountCommandFailure(this.accountId, this.error)
    }
}
