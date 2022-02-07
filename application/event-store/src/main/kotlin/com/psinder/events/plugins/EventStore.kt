package com.psinder.events.plugins

import com.psinder.events.config.EventStoreConfig
import io.ktor.application.Application
import io.ktor.application.feature
import io.ktor.application.install
import io.traxter.eventstoredb.EventStoreDB
import org.koin.dsl.module

internal fun Application.configureEventStore(eventStoreConfig: EventStoreConfig) {
    install(EventStoreDB) {
        connectionString = eventStoreConfig.connectionString
    }
}

val Application.eventStoreDb
    get() = feature(EventStoreDB)

val Application.eventStoreDbKoinModule
    get() = module { single { eventStoreDb } }
