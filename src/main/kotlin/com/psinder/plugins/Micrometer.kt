package com.psinder.plugins

import com.psinder.config.ConfigKey
import com.psinder.config.TracingConfig
import com.psinder.config.getPropertyOrNull
import io.ktor.application.*
import io.ktor.metrics.micrometer.*
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.datadog.DatadogConfig
import io.micrometer.datadog.DatadogMeterRegistry
import io.micrometer.core.instrument.Clock

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
