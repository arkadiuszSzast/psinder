package com.psinder.dog.commands

import com.psinder.auth.account.AccountIdProvider
import com.psinder.dog.DogProfileDto
import com.psinder.dog.requests.RegisterDogRequest
import com.trendyol.kediatr.CommandMetadata
import com.trendyol.kediatr.CommandWithResult
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

data class RegisterDogCommand(
    val registerDogRequest: RegisterDogRequest,
    val accountIdProvider: AccountIdProvider,
    override val metadata: CommandMetadata? = null
) : CommandWithResult<RegisterDogCommandResult>

@Serializable
data class RegisterDogCommandResult(@Contextual val dogId: Id<DogProfileDto>)
