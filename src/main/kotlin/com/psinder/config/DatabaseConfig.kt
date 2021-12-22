package com.psinder.config

object DatabaseConfig {
    val name = getProperty(Keys.name)
    val connectionString = getProperty(Keys.connectionString)

    private object Keys {
        val name = ConfigKey("database.name")
        val connectionString = ConfigKey("database.connectionString")
    }
}

