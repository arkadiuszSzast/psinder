package com.psinder.dog

import com.psinder.auth.getAccountContext
import com.psinder.dog.commands.RegisterDogCommand
import com.psinder.dog.requests.RegisterDogRequest
import com.psinder.file.storage.commands.UploadFileCommand
import com.psinder.shared.validation.validateEagerly
import com.trendyol.kediatr.CommandBus
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.Principal
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.principal
import io.ktor.http.Url
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import org.koin.ktor.ext.inject

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

        get(DogApi.v1) {
            val candidate =
                DogProfileImage.getCandidate(Url("https://ichef.bbci.co.uk/news/976/cpsprodpb/D79D/production/_123979155_gettyimages-1064733482.jpg"))
            commandBus.executeCommandAsync(UploadFileCommand(candidate))
            call.respond("UPLOADED")
        }
    }
}
