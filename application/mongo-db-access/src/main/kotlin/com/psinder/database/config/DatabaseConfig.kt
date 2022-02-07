package com.psinder.database.config

import com.psinder.shared.config.ConfigKey
import com.psinder.shared.config.getProperty

internal object DatabaseConfig {
    val name by lazy { getProperty(Keys.name) }
    val connectionString by lazy { getProperty(Keys.connectionString) }

    private object Keys {
        val name = ConfigKey("database.name")
        val connectionString = ConfigKey("database.connectionString")
    }
}
