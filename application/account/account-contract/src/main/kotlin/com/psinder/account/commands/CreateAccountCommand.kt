package com.psinder.account.commands

import com.psinder.account.AccountDto
import com.psinder.account.requests.CreateAccountRequest
import com.trendyol.kediatr.CommandMetadata
import com.trendyol.kediatr.CommandWithResult
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

data class CreateAccountCommand(
    val createAccountRequest: CreateAccountRequest,
    override val metadata: CommandMetadata? = null
) : CommandWithResult<CreateAccountCommandResult>

@Serializable
data class CreateAccountCommandResult(@Contextual val accountId: Id<AccountDto>)
