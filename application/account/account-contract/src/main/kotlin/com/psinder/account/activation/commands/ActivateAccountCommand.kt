package com.psinder.account.activation.commands

import com.psinder.account.AccountDto
import com.psinder.account.activation.AccountActivationError
import com.psinder.shared.jwt.JwtToken
import com.trendyol.kediatr.CommandMetadata
import com.trendyol.kediatr.CommandWithResult
import org.litote.kmongo.Id
import pl.brightinventions.codified.enums.CodifiedEnum

data class ActivateAccountCommand(val token: JwtToken, override val metadata: CommandMetadata? = null) :
    CommandWithResult<ActivateAccountCommandResult>

sealed class ActivateAccountCommandResult

data class ActivateAccountCommandSucceed(val accountId: Id<AccountDto>) : ActivateAccountCommandResult()

data class ActivateAccountCommandFailure(
    val accountId: Id<AccountDto>,
    val errorCode: CodifiedEnum<AccountActivationError, String>
) : ActivateAccountCommandResult()
