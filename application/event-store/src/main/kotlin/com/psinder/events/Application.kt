package com.psinder.events

import com.psinder.events.config.EventStoreConfig
import com.psinder.events.plugins.configureEventStore
import io.ktor.application.Application

@Suppress("unused")
fun Application.eventStoreModule() {
    configureEventStore(EventStoreConfig)
}
