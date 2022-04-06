package com.psinder.account.activation.commands

import com.psinder.account.AccountDto
import com.psinder.shared.jwt.JwtToken
import com.trendyol.kediatr.CommandMetadata
import com.trendyol.kediatr.CommandWithResult
import org.litote.kmongo.Id

data class GenerateAccountActivationTokenCommand(
    val accountId: Id<AccountDto>,
    override val metadata: CommandMetadata? = null
) : CommandWithResult<GenerateAccountActivationTokenCommandResult>

data class GenerateAccountActivationTokenCommandResult(val token: JwtToken)
