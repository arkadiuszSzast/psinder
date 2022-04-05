package com.psinder.account.activation.commands

import com.psinder.account.AccountDto
import com.psinder.shared.jwt.JwtToken
import com.trendyol.kediatr.CommandMetadata
import com.trendyol.kediatr.CommandWithResult
import org.litote.kmongo.Id

data class GenerateAccountActivateTokenCommand(
    val accountId: Id<AccountDto>,
    override val metadata: CommandMetadata? = null
) :
    CommandWithResult<GenerateAccountActivateTokenCommandResult>

data class GenerateAccountActivateTokenCommandResult(val token: JwtToken)
