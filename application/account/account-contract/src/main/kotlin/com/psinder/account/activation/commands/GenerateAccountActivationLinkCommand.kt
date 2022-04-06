package com.psinder.account.activation.commands

import com.psinder.account.AccountDto
import com.trendyol.kediatr.CommandMetadata
import com.trendyol.kediatr.CommandWithResult
import io.ktor.http.Url
import org.litote.kmongo.Id

data class GenerateAccountActivationLinkCommand(
    val accountId: Id<AccountDto>,
    override val metadata: CommandMetadata? = null
) : CommandWithResult<GenerateAccountActivationLinkCommandResult>

data class GenerateAccountActivationLinkCommandResult(val link: Url)
