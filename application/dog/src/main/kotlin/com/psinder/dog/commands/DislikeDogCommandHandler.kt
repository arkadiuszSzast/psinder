package com.psinder.dog.commands

import com.psinder.dog.DogAggregate
import com.psinder.dog.dislikeDog
import com.psinder.dog.queries.FindDogProfileByIdQuery
import com.psinder.events.streamName
import com.psinder.events.toEventData
import com.trendyol.kediatr.AsyncCommandHandler
import com.trendyol.kediatr.CommandBus
import io.traxter.eventstoredb.EventStoreDB
import mu.KotlinLogging
import org.bson.types.ObjectId
import org.litote.kmongo.id.toId
import org.litote.kmongo.toId

class DislikeDogCommandHandler(
    private val commandBus: CommandBus,
    private val eventStoreDB: EventStoreDB
) : AsyncCommandHandler<LikeDogCommand> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(command: LikeDogCommand) {
        val (dogContext, targetDogId, metadata) = command

        val dog = commandBus.executeQueryAsync(FindDogProfileByIdQuery(ObjectId(dogContext.dogId.value).toId())).dog

        dog
            .tapNone { logger.warn { "Dog with id ${dogContext.dogId} not found. Dislike wont be saved." } }
            .tap {
                DogAggregate.Events.dislikeDog(dogContext, it.votes, targetDogId)
                    .fold(
                        {
                            logger.warn { "Error when trying to dislike dog with id $targetDogId. Error: ${it.stackTraceToString()}" }
                        },
                        {
                            eventStoreDB.appendToStream(
                                it.streamName,
                                it.toEventData(metadata),
                            )
                        }
                    )
            }
    }
}
