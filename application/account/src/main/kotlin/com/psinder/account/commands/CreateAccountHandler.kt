package com.psinder.account.commands

import arrow.core.nel
import com.psinder.account.Account
import com.psinder.account.AccountCreatedEvent
import com.psinder.account.queries.FindAccountByEmailQuery
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.events.toEventData
import com.psinder.shared.validation.ValidationError
import com.psinder.shared.validation.ValidationException
import com.trendyol.kediatr.AsyncCommandWithResultHandler
import com.trendyol.kediatr.CommandBus
import io.traxter.eventstoredb.EventStoreDB
import mu.KotlinLogging

internal class CreateAccountHandler(
    private val commandBus: CommandBus,
    private val eventStore: EventStoreDB,
    private val acl: AuthorizedAccountAbilityProvider
) : AsyncCommandWithResultHandler<CreateAccountCommand, CreateAccountCommandResult> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(command: CreateAccountCommand): CreateAccountCommandResult {
        acl.canCreate(Account::class.java)
        logger.debug { "Starting creating account" }
        val (personalData, email, rawPassword, timeZoneId) = command.createAccountRequest

        val alreadyRegisteredAccount = commandBus.executeQueryAsync(FindAccountByEmailQuery(email)).accountDto
        alreadyRegisteredAccount.tap {
            logger.error { "Cannot create account. Email [${it.email}] is already taken" }
            throw ValidationException(
                ValidationError(
                    ".email",
                    "validation.email_already_taken"
                ).nel()
            )
        }
        val accountCreatedEvent = Account.create(email, personalData, rawPassword.hashpw(), timeZoneId)

        logger.debug { "Account created. Sending event: $accountCreatedEvent" }
        eventStore.appendToStream(
            accountCreatedEvent.eventFamily.code,
            accountCreatedEvent.toEventData<AccountCreatedEvent>()
        )

        return CreateAccountCommandResult(accountCreatedEvent.accountId.cast())
    }
}
