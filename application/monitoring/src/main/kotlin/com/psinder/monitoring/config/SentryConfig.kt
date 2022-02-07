package com.psinder.monitoring.config

import com.psinder.shared.config.ConfigKey
import com.psinder.shared.config.getProperty
import com.psinder.shared.config.getPropertyAsBoolean

object SentryConfig {
    val enabled by lazy { getPropertyAsBoolean(Keys.enabled) }
    val dsn by lazy { getProperty(Keys.dsn) }

    private object Keys {
        val enabled = ConfigKey("sentry.enabled")
        val dsn = ConfigKey("sentry.dsn")
    }
}
