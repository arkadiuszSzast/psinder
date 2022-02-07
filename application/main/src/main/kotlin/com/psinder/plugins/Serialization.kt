package com.psinder.plugins

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.serialization.json
import kotlinx.serialization.json.Json

internal fun Application.configureSerialization(jsonMapper: Json) {

    install(ContentNegotiation) {
        json(jsonMapper)
    }
}
