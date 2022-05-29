package com.psinder.dog

import com.eventstore.dbclient.ReadStreamOptions
import com.eventstore.dbclient.SubscribeToStreamOptions
import com.psinder.auth.account.DogId
import com.psinder.auth.getAccountContext
import com.psinder.dog.commands.ImpersonateDogCommand
import com.psinder.dog.commands.ImpersonateDogCommandFailureResult
import com.psinder.dog.commands.ImpersonateDogCommandSuccessResult
import com.psinder.dog.commands.LikeDogCommand
import com.psinder.dog.commands.RegisterDogCommand
import com.psinder.dog.events.DogPairFoundEvent
import com.psinder.dog.queries.FindNotVotedDogsQuery
import com.psinder.dog.requests.DislikeDogRequest
import com.psinder.dog.requests.LikeDogRequest
import com.psinder.dog.requests.RegisterDogRequest
import com.psinder.events.fullEventType
import com.psinder.events.getAs
import com.psinder.file.storage.commands.UploadFileCommand
import com.psinder.shared.range
import com.psinder.shared.sse.SseEvent
import com.psinder.shared.sse.respondSse
import com.psinder.shared.validation.validateEagerly
import com.trendyol.kediatr.CommandBus
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.traxter.eventstoredb.StreamName
import io.traxter.eventstoredb.eventStoreDb
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.broadcast
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject
import org.litote.kmongo.id.toId
import org.litote.kmongo.toId

fun Application.configureDogRouting() {

    val commandBus: CommandBus by inject()

    routing {

        authenticate {
            post(DogApi.v1) {
                val request = call.receive<RegisterDogRequest>().validateEagerly()
                val accountContext = call.getAccountContext()
                val result = commandBus.executeCommandAsync(RegisterDogCommand(request, accountContext))
                call.respond(result)
            }
        }

        authenticate {
            post(DogApi.v1 + "/{dogId}/impersonate") {
                val dogId = call.parameters["dogId"]!!.let { ObjectId(it).toId<DogProfileDto>() }
                val accountContext = call.getAccountContext()
                val result = commandBus.executeCommandAsync(ImpersonateDogCommand(dogId, accountContext))

                when (result) {
                    is ImpersonateDogCommandSuccessResult -> call.respond(result)
                    is ImpersonateDogCommandFailureResult -> call.respond(HttpStatusCode.BadRequest, result)
                }
            }
        }

        authenticate {
            get(DogApi.v1 + "/not-voted") {
                val dogContext = call.getDogContext()
                val range = call.request.range()
                val result = commandBus.executeQueryAsync(FindNotVotedDogsQuery(dogContext, range))

                call.respond(result)
            }
        }

        authenticate {
            post(DogApi.v1 + "/votes/like") {
                val dogContext = call.getDogContext()
                val request = call.receive<LikeDogRequest>()

                commandBus.executeCommandAsync(LikeDogCommand(dogContext, request.targetDogId))
                call.respond(HttpStatusCode.OK)
            }
        }

        authenticate {
            post(DogApi.v1 + "/votes/dislike") {
                val dogContext = call.getDogContext()
                val request = call.receive<DislikeDogRequest>()

                commandBus.executeCommandAsync(LikeDogCommand(dogContext, request.targetDogId))
                call.respond(HttpStatusCode.OK)
            }
        }

        get(DogApi.v1 + "/events/pair-found") {
            val dogContext = call.getDogContext()
            val channel = Channel<SseEvent<DogPairFoundEvent>>().broadcast()
            val events = channel.openSubscription()
            val lastRevision = eventStoreDb.readStream(
                StreamName(DogPairFoundEvent.fullEventType.streamName()),
                ReadStreamOptions.get().fromStart()
            ).events.last().event.streamRevision

            eventStoreDb.subscribeToStream(
                StreamName(DogPairFoundEvent.fullEventType.streamName()),
                SubscribeToStreamOptions.get().resolveLinkTos().fromRevision(lastRevision)
            ) {
                val dogPairFoundEvent = this.event
                dogPairFoundEvent.getAs<DogPairFoundEvent>()
                    .tap {
                        if (DogId(it.dogId.toString()) == dogContext.dogId || DogId(it.pairDogId.toString()) == dogContext.dogId) {
                            channel.send(SseEvent(it, it.fullEventType.get(), it.eventId.toString().toId()))
                        }
                    }
            }

            try {
                call.respondSse(events)
            } finally {
                events.cancel()
            }
        }

        get(DogApi.v1) {
            val candidate =
                DogProfileImage.getCandidate(Url("https://ichef.bbci.co.uk/news/976/cpsprodpb/D79D/production/_123979155_gettyimages-1064733482.jpg"))
            commandBus.executeCommandAsync(UploadFileCommand(candidate))
            call.respond("UPLOADED")
        }
    }
}
