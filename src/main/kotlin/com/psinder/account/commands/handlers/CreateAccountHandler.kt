package com.psinder.account.commands.handlers

import an.awesome.pipelinr.Command
import an.awesome.pipelinr.Pipeline
import com.psinder.account.Account
import com.psinder.account.AccountCreatedEvent
import com.psinder.account.commands.CreateAccountCommand
import com.psinder.account.commands.CreateAccountCommandResult
import com.psinder.account.queries.FindAccountByEmailQuery
import com.psinder.shared.events.toEventData
import io.traxter.eventstoredb.EventStoreDB
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

internal class CreateAccountHandler(
    private val pipeline: Pipeline,
    private val eventStore: EventStoreDB
) : Command.Handler<CreateAccountCommand, CreateAccountCommandResult> {

    override fun handle(command: CreateAccountCommand): CreateAccountCommandResult = runBlocking {
        logger.debug { "Starting creating account" }
        val (personalData, email, password, timeZoneId) = command.createAccountRequest

        val isEmailAlreadyTaken = pipeline.send(FindAccountByEmailQuery(email)).accountDto.isDefined()
        if (isEmailAlreadyTaken) {
            throw IllegalStateException("Cannot create account with already existing email")
        }

        val account = Account(email, personalData, password, timeZoneId)
        val accountCreatedEvent = AccountCreatedEvent(account)

        logger.debug { "Account created. Sending event: $accountCreatedEvent" }
        eventStore.appendToStream(accountCreatedEvent.eventFamily.code, accountCreatedEvent.toEventData())
        pipeline.send(accountCreatedEvent)

        CreateAccountCommandResult(account.id)
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
