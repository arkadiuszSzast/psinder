package com.psinder.feature.toggle

import com.configcat.ConfigCatClient
import com.configcat.PollingModes
import com.psinder.feature.toggle.config.ConfigCatConfig
import org.koin.dsl.module

private const val AUTO_PULL_INTERVAL_IN_SEC = 60

val configCatModule = module {
    single {
        ConfigCatClient.Builder()
            .mode(PollingModes.autoPoll(AUTO_PULL_INTERVAL_IN_SEC))
            .build(ConfigCatConfig.apiKey)
    }
}
