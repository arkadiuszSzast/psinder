package com.psinder.account.commands.handlers

import an.awesome.pipelinr.Command
import com.psinder.account.commands.CreateAccountCommand
import com.psinder.account.commands.CreateAccountCommandResult
import mu.KotlinLogging

internal class CreateAccountHandler : Command.Handler<CreateAccountCommand, CreateAccountCommandResult> {
    private val logger = KotlinLogging.logger {}

    override fun handle(command: CreateAccountCommand): CreateAccountCommandResult {
        logger.debug { "Starting creating account" }

        return CreateAccountCommandResult("123")
    }
}
