package com.psinder.account.commands

import com.psinder.account.AccountDto
import com.psinder.account.requests.CreateAccountRequest
import com.trendyol.kediatr.CommandWithResult
import org.litote.kmongo.Id

data class CreateAccountCommand(val createAccountRequest: CreateAccountRequest) :
    CommandWithResult<CreateAccountCommandResult>

data class CreateAccountCommandResult(val accountId: Id<AccountDto>)
