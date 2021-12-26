package com.psinder.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*

internal fun Application.configureHTTP() {
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        allowCredentials = false
        anyHost()
    }

    install(PartialContent) {
        maxRangeCount = 10
    }

}
