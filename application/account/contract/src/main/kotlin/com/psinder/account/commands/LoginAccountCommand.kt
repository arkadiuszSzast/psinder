package com.psinder.account.commands

import com.psinder.account.requests.LoginAccountRequest
import com.psinder.shared.jwt.JwtToken
import com.trendyol.kediatr.CommandWithResult

data class LoginAccountCommand(val loginAccountRequest: LoginAccountRequest) :
    CommandWithResult<LoginAccountCommandResult>

data class LoginAccountCommandResult(val token: JwtToken)
