package com.psinder.config

object EventStoreConfig {

    val connectionString by lazy { getProperty(Keys.connectionString) }

    private object Keys {
        val connectionString = ConfigKey("eventStore.connectionString")
    }
}
