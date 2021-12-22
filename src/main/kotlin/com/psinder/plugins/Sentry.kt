package com.psinder.plugins

import com.psinder.config.ApplicationConfig
import com.psinder.config.SentryConfig
import com.psinder.features.SentryFeature
import io.ktor.application.*

fun Application.initializeSentry() {
    if (!SentryConfig.enabled) {
        return
    }

    install(SentryFeature) {
        dsn = SentryConfig.dsn
        appEnv = ApplicationConfig.environment
        serverName = "psinder"
    }
}