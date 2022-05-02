package com.psinder.dog

import com.psinder.shared.file.FileStorage
import com.trendyol.kediatr.CommandBus
import io.ktor.application.Application
import io.ktor.http.Url
import io.ktor.routing.get
import io.ktor.routing.routing
import org.koin.ktor.ext.inject

fun Application.configureDogRouting() {

    val commandBus: CommandBus by inject()
    val storage: FileStorage by inject()

    routing {
        get(DogApi.v1) {
            storage.upload(DogProfileImage.getCandidate(Url("https://cdn.jpegmini.com/user/images/slider_puffin_before_mobile.jpg")))
        }
    }
}
