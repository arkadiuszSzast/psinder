package com.psinder.events

import com.eventstore.dbclient.SubscribeToStreamOptions
import io.traxter.eventstoredb.EventListener
import io.traxter.eventstoredb.EventStoreDB

suspend fun EventStoreDB.subscribeByFamily(family: EventFamily, fromRevision: Long = 0, listener: EventListener) =
    this.subscribeToStream(
        family.streamName,
        SubscribeToStreamOptions.get().resolveLinkTos().fromRevision(fromRevision),
        listener
    )
