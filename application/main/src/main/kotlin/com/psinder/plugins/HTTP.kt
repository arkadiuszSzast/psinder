package com.psinder.plugins

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.PartialContent
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod

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
    }
}
