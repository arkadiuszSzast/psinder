package com.psinder

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing

fun Application.configureApplicationStatusRouting() {

    routing {
        get() {
            call.respond(HttpStatusCode.OK, "Psinder App")
        }
        get("${ApplicationStatusApi.v1}/health") {
            call.respond(HttpStatusCode.OK, "Healthy")
        }
    }
}
