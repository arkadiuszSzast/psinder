package com.psinder.monitoring.plugins

import com.psinder.monitoring.config.SentryConfig
import com.psinder.monitoring.features.SentryFeature
import com.psinder.shared.config.ApplicationConfig
import io.ktor.application.Application
import io.ktor.application.install

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
