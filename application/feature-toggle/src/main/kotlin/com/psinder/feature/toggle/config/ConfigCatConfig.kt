package com.psinder.feature.toggle.config

import com.psinder.shared.config.ConfigKey
import com.psinder.shared.config.getProperty

object ConfigCatConfig {
    val apiKey by lazy { getProperty(Keys.apiKey) }
    val basicAuthUsername by lazy { getProperty(Keys.basicAuthUsername) }
    val basicAuthPassword by lazy { getProperty(Keys.basicAuthPassword) }

    private object Keys {
        val apiKey = ConfigKey("configCat.apiKey")
        val basicAuthUsername = ConfigKey("configCat.basicAuth.username")
        val basicAuthPassword = ConfigKey("configCat.basicAuth.password")
    }
}
