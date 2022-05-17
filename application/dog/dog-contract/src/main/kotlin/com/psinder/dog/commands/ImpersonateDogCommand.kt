package com.psinder.dog.commands

import com.psinder.auth.account.AccountContext
import com.psinder.dog.DogProfileDto
import com.psinder.dog.ImpersonatingError
import com.psinder.shared.jwt.JwtToken
import com.trendyol.kediatr.CommandMetadata
import com.trendyol.kediatr.CommandWithResult
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import pl.brightinventions.codified.enums.CodifiedEnum

data class ImpersonateDogCommand(
    val dogId: Id<DogProfileDto>, val accountContext: AccountContext, override val metadata: CommandMetadata? = null
) : CommandWithResult<ImpersonateDogCommandResult>

@Serializable
sealed class ImpersonateDogCommandResult

@Serializable
data class ImpersonateDogCommandSuccessResult(val token: JwtToken) : ImpersonateDogCommandResult()

@Serializable
data class ImpersonateDogCommandFailureResult(
    @Serializable(with = ImpersonatingError.CodifiedSerializer::class)
    val error: CodifiedEnum<ImpersonatingError, String>
) : ImpersonateDogCommandResult()
