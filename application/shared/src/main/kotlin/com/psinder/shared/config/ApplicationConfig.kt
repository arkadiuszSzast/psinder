package com.psinder.shared.config

object ApplicationConfig {
    val environment by lazy { getProperty(Keys.environment) }

    private object Keys {
        val environment = ConfigKey("application.env")
    }
}