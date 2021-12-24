package com.psinder.config

object SentryConfig {
    val enabled by lazy { getPropertyAsBoolean(Keys.enabled) }
    val dsn by lazy { getProperty(Keys.dsn) }

    private object Keys {
        val enabled = ConfigKey("sentry.enabled")
        val dsn = ConfigKey("sentry.dsn")
    }
}