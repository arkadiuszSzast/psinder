package com.psinder.dog

import com.psinder.file.storage.commands.UploadFileCommand
import com.trendyol.kediatr.CommandBus
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.Url
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import org.koin.ktor.ext.inject

fun Application.configureDogRouting() {

    val commandBus: CommandBus by inject()

    routing {
        get(DogApi.v1) {
            val candidate =
                DogProfileImage.getCandidate(Url("https://ichef.bbci.co.uk/news/976/cpsprodpb/D79D/production/_123979155_gettyimages-1064733482.jpg"))
            commandBus.executeCommandAsync(UploadFileCommand(candidate))
            call.respond("UPLOADED")
        }
    }
}
