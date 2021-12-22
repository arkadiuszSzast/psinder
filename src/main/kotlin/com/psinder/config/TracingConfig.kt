package com.psinder.config

object TracingConfig {
    val enabled = getPropertyAsBoolean(Keys.enabled)

    private object Keys {
        val enabled = ConfigKey("tracing.enabled")
    }
}