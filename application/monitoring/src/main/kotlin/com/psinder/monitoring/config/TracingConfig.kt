package com.psinder.monitoring.config

import com.psinder.shared.config.ConfigKey
import com.psinder.shared.config.getPropertyAsBoolean

object TracingConfig {
    val enabled by lazy { getPropertyAsBoolean(Keys.enabled) }

    private object Keys {
        val enabled = ConfigKey("tracing.enabled")
    }
}
