package com.psinder.account.commands.handlers

import com.psinder.account.Account
import com.psinder.account.AccountCreatedEvent
import com.psinder.account.commands.CreateAccountCommand
import com.psinder.account.commands.CreateAccountCommandResult
import com.psinder.account.queries.FindAccountByEmailQuery
import com.psinder.events.toEventData
import com.trendyol.kediatr.AsyncCommandWithResultHandler
import com.trendyol.kediatr.CommandBus
import io.traxter.eventstoredb.EventStoreDB
import mu.KotlinLogging

internal class CreateAccountHandler(
    private val commandBus: CommandBus,
    private val eventStore: EventStoreDB
) : AsyncCommandWithResultHandler<CreateAccountCommand, CreateAccountCommandResult> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(command: CreateAccountCommand): CreateAccountCommandResult {
        logger.debug { "Starting creating account" }
        val (personalData, email, rawPassword, timeZoneId) = command.createAccountRequest

        val isEmailAlreadyTaken = commandBus.executeQueryAsync(FindAccountByEmailQuery(email)).accountDto.isDefined()
        if (isEmailAlreadyTaken) {
            throw IllegalStateException("Cannot create account with already existing email")
        }

        val account = Account(email, personalData, rawPassword.hashpw(), timeZoneId)
        val accountCreatedEvent = AccountCreatedEvent(account)

        logger.debug { "Account created. Sending event: $accountCreatedEvent" }
        eventStore.appendToStream(
            accountCreatedEvent.eventFamily.code,
            accountCreatedEvent.toEventData<AccountCreatedEvent>()
        )

        return CreateAccountCommandResult(account.id)
    }
}
