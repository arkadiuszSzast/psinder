package com.psinder.monitoring.plugins

import com.psinder.monitoring.config.TracingConfig
import com.psinder.shared.config.ConfigKey
import com.psinder.shared.config.getPropertyOrNull
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.metrics.micrometer.MicrometerMetrics
import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.datadog.DatadogConfig
import io.micrometer.datadog.DatadogMeterRegistry

internal fun Application.configureMicrometer(tracingConfig: TracingConfig) {
    if (!tracingConfig.enabled) {
        return
    }

    val datadogConfig = DatadogConfig { key -> getPropertyOrNull(ConfigKey(key)) }

    val meterRegistry: MeterRegistry = DatadogMeterRegistry(datadogConfig, Clock.SYSTEM)
    install(MicrometerMetrics) {
        registry = meterRegistry
    }
}
