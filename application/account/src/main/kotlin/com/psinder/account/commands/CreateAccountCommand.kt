package com.psinder.account.commands

import com.psinder.account.Account
import com.psinder.account.requests.CreateAccountRequest
import com.trendyol.kediatr.CommandWithResult
import org.litote.kmongo.Id

internal data class CreateAccountCommand(val createAccountRequest: CreateAccountRequest) :
    CommandWithResult<CreateAccountCommandResult>

internal data class CreateAccountCommandResult(val accountId: Id<Account>)
