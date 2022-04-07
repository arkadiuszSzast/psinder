package com.psinder.account.activation

import com.psinder.account.AccountDto
import com.psinder.account.activation.commands.AccountActivationError
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import pl.brightinventions.codified.enums.CodifiedEnum

sealed class ActivateAccountResponse

@Serializable
data class ActivateAccountSuccessfullyResponse(@Contextual val accountId: Id<AccountDto>) : ActivateAccountResponse()

@Serializable
data class ActivateAccountFailedResponse(
    @Contextual val accountId: Id<AccountDto>,
    @Serializable(with = AccountActivationError.CodifiedSerializer::class)
    val error: CodifiedEnum<AccountActivationError, String>
) : ActivateAccountResponse()
