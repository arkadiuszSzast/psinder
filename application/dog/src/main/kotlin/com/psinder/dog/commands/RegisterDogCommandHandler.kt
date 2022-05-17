package com.psinder.dog.commands

import com.psinder.auth.authority.registerDogFeature
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.dog.DogAggregate
import com.psinder.dog.DogProfileImage
import com.psinder.dog.register
import com.psinder.events.streamName
import com.psinder.events.toEventData
import com.psinder.file.storage.commands.UploadFileCommand
import com.trendyol.kediatr.AsyncCommandWithResultHandler
import com.trendyol.kediatr.CommandBus
import io.ktor.http.Url
import io.traxter.eventstoredb.EventStoreDB
import mu.KotlinLogging

internal class RegisterDogCommandHandler(
    private val commandBus: CommandBus,
    private val eventStore: EventStoreDB,
    private val acl: AuthorizedAccountAbilityProvider,
) : AsyncCommandWithResultHandler<RegisterDogCommand, RegisterDogCommandResult> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(command: RegisterDogCommand): RegisterDogCommandResult {
        acl.ensure().hasAccessTo(registerDogFeature)
        val (registerDogRequest, accountIdProvider, metadata) = command
        val (dogName, dogDescription, imagesUrls) = registerDogRequest

        val images = imagesUrls
            .map { DogProfileImage.getCandidate(Url(it.value)) }
            .map { commandBus.executeCommandAsync(UploadFileCommand(it, metadata)).result }
            .mapNotNull { it.orNull() }
            .map { DogProfileImage.fromStoredFile(it) }

        val dogRegisteredEvent = DogAggregate.Events.register(accountIdProvider, dogName, dogDescription, images)
        logger.debug { "Dog registered. Sending event: $dogRegisteredEvent" }

        eventStore.appendToStream(
            dogRegisteredEvent.streamName,
            dogRegisteredEvent.toEventData(metadata),
        )

        return RegisterDogCommandResult(dogRegisteredEvent.dogId)
    }
}
