package com.psinder.config

object SentryConfig {
    val enabled = getPropertyAsBoolean(Keys.enabled)
    val dsn = getProperty(Keys.dsn)

    private object Keys {
        val enabled = ConfigKey("sentry.enabled")
        val dsn = ConfigKey("sentry.dsn")
    }
}