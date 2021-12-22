package com.psinder.plugins

import com.psinder.config.JwtConfig
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("HTTP version is ${call.request.httpVersion}")
        }
        get("/secret") {
            call.respondText("Secret: ${JwtConfig.secret}")
        }
    }
}