package com.psinder.config

object TracingConfig {
    val enabled by lazy { getPropertyAsBoolean(Keys.enabled) }

    private object Keys {
        val enabled = ConfigKey("tracing.enabled")
    }
}