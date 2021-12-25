package com.psinder.plugins

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import org.koin.ktor.ext.inject

internal fun Application.configureSerialization() {
    val objectMapper: ObjectMapper by inject()

    install(ContentNegotiation) {
        register(ContentType.Application.Json, JacksonConverter(objectMapper))
    }
}
