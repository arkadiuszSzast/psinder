package com.psinder.events.config

import com.psinder.shared.config.ConfigKey
import com.psinder.shared.config.getProperty

internal object EventStoreConfig {

    val connectionString by lazy { getProperty(Keys.connectionString) }

    private object Keys {
        val connectionString = ConfigKey("eventStore.connectionString")
    }
}
