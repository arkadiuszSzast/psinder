package com.psinder.plugins

import com.psinder.config.EventStoreConfig
import io.ktor.application.*
import io.traxter.eventstoredb.EventStoreDB
import org.koin.dsl.module
import org.koin.ktor.ext.modules

internal fun Application.configureEventStore(eventStoreConfig: EventStoreConfig) {
    val client = install(EventStoreDB) {
        connectionString = eventStoreConfig.connectionString
    }

    modules(module { single { client } })
}
