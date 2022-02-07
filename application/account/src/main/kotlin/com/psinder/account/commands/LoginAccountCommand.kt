package com.psinder.account.commands

import com.psinder.account.requests.LoginAccountRequest
import com.psinder.shared.jwt.JwtToken
import com.trendyol.kediatr.CommandWithResult

internal data class LoginAccountCommand(val loginAccountRequest: LoginAccountRequest) :
    CommandWithResult<LoginAccountCommandResult>

internal data class LoginAccountCommandResult(val token: JwtToken)
