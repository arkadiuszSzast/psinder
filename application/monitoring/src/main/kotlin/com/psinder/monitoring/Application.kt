package com.psinder.monitoring

import com.psinder.monitoring.config.SentryConfig
import com.psinder.monitoring.config.TracingConfig
import com.psinder.monitoring.plugins.configureMicrometer
import com.psinder.monitoring.plugins.configureMonitoring
import com.psinder.monitoring.plugins.configureOpentracing
import com.psinder.monitoring.plugins.initializeSentry
import io.ktor.application.Application

@Suppress("unused")
fun Application.monitoringModule() {
    configureOpentracing(TracingConfig)
    initializeSentry(SentryConfig)
    configureMicrometer(TracingConfig)
    configureMonitoring()
}
