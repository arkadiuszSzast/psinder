package com.psinder.config

object ApplicationConfig {
    val environment = getProperty(Keys.environment)

    private object Keys {
        val environment = ConfigKey("application.env")
    }
}