package com.psinder.events

import com.trendyol.kediatr.CommandMetadata
import io.traxter.eventstoredb.EventStoreDB

suspend inline fun <reified T : DomainEvent> EventStoreDB.appendToStream(event: T, metadata: CommandMetadata? = null) =
    this.appendToStream(event.streamName, event.toEventData<T>(metadata))
