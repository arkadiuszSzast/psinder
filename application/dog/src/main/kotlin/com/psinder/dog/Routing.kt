package com.psinder.dog

import com.psinder.auth.getAccountContext
import com.psinder.dog.commands.ImpersonateDogCommand
import com.psinder.dog.commands.ImpersonateDogCommandFailureResult
import com.psinder.dog.commands.ImpersonateDogCommandSuccessResult
import com.psinder.dog.commands.LikeDogCommand
import com.psinder.dog.commands.RegisterDogCommand
import com.psinder.dog.requests.DislikeDogRequest
import com.psinder.dog.requests.LikeDogRequest
import com.psinder.dog.requests.RegisterDogRequest
import com.psinder.file.storage.commands.UploadFileCommand
import com.psinder.shared.validation.validateEagerly
import com.trendyol.kediatr.CommandBus
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject
import org.litote.kmongo.id.toId

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

        get(DogApi.v1) {
            val candidate =
                DogProfileImage.getCandidate(Url("https://ichef.bbci.co.uk/news/976/cpsprodpb/D79D/production/_123979155_gettyimages-1064733482.jpg"))
            commandBus.executeCommandAsync(UploadFileCommand(candidate))
            call.respond("UPLOADED")
        }
    }
}
