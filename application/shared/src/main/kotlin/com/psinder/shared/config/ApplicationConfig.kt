package com.psinder.shared.config

object ApplicationConfig {
    val environment by lazy { getProperty(Keys.environment) }
    val selfUrl by lazy { getProperty(Keys.selfUrl) }
    val webClientAppUrl by lazy { getProperty(Keys.webClientAppUrl) }

    private object Keys {
        val environment = ConfigKey("application.env")
        val selfUrl = ConfigKey("application.selfUrl")
        val webClientAppUrl = ConfigKey("application.webClientAppUrl")
    }
}
