package com.psinder.plugins

import com.psinder.config.ApplicationConfig
import com.psinder.config.SentryConfig
import com.psinder.features.SentryFeature
import io.ktor.application.*

internal fun Application.initializeSentry(sentryConfig: SentryConfig) {
    if (!sentryConfig.enabled) {
        return
    }

    install(SentryFeature) {
        dsn = sentryConfig.dsn
        appEnv = ApplicationConfig.environment
        serverName = "psinder"
    }
}
