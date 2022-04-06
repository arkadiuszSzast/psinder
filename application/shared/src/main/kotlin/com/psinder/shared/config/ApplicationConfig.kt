package com.psinder.shared.config

object ApplicationConfig {
    val environment by lazy { getProperty(Keys.environment) }
    val selfUrl by lazy { getProperty(Keys.selfUrl) }

    private object Keys {
        val environment = ConfigKey("application.env")
        val selfUrl = ConfigKey("application.selfUrl")
    }
}
