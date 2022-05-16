package com.psinder.dog.commands

import com.trendyol.kediatr.AsyncCommandWithResultHandler
import org.litote.kmongo.newId

internal class RegisterDogCommandHandler : AsyncCommandWithResultHandler<RegisterDogCommand, RegisterDogCommandResult> {
    override suspend fun handleAsync(command: RegisterDogCommand): RegisterDogCommandResult {
        return RegisterDogCommandResult(newId())
    }
}