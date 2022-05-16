package com.psinder.dog.commands

import com.psinder.auth.account.AccountContext
import com.psinder.dog.DogDto
import com.psinder.dog.requests.RegisterDogRequest
import com.trendyol.kediatr.CommandMetadata
import com.trendyol.kediatr.CommandWithResult
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

data class RegisterDogCommand(
    val registerDogRequest: RegisterDogRequest,
    val accountContext: AccountContext,
    override val metadata: CommandMetadata? = null
) : CommandWithResult<RegisterDogCommandResult>

@Serializable
data class RegisterDogCommandResult(@Contextual val dogId: Id<DogDto>)
