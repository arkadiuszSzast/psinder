package com.psinder.account.commands

import an.awesome.pipelinr.Command
import com.psinder.account.requests.LoginAccountRequest
import com.psinder.shared.jwt.JwtToken

internal data class LoginAccountCommand(val loginAccountRequest: LoginAccountRequest) :
    Command<LoginAccountCommandResult>

internal data class LoginAccountCommandResult(val token: JwtToken)
