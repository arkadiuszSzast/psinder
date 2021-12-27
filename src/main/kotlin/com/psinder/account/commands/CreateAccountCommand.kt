package com.psinder.account.commands

import an.awesome.pipelinr.Command
import com.psinder.account.requests.CreateAccountRequest

internal data class CreateAccountCommand(val createAccountRequest: CreateAccountRequest) :
    Command<CreateAccountCommandResult>

internal data class CreateAccountCommandResult(val accountId: String)
