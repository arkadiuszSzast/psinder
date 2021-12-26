package com.psinder.config

object DatabaseConfig {
    val name by lazy { getProperty(Keys.name) }
    val connectionString by lazy { getProperty(Keys.connectionString) }

    private object Keys {
        val name = ConfigKey("database.name")
        val connectionString = ConfigKey("database.connectionString")
    }
}
