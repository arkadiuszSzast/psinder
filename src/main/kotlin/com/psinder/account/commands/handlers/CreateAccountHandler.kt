package com.psinder.account.commands.handlers

import an.awesome.pipelinr.Command
import com.psinder.account.Account
import com.psinder.account.commands.CreateAccountCommand
import com.psinder.account.commands.CreateAccountCommandResult
import mu.KotlinLogging

internal class CreateAccountHandler : Command.Handler<CreateAccountCommand, CreateAccountCommandResult> {

    override fun handle(command: CreateAccountCommand): CreateAccountCommandResult {
        logger.debug { "Starting creating account" }
        val (personalData, email, password, timeZoneId) = command.createAccountRequest
        val account = Account(email, personalData, password)

        return CreateAccountCommandResult("123")
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
