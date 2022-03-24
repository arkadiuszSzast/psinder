package com.psinder.database

import com.eventstore.dbclient.EventStoreDBClient
import com.eventstore.dbclient.EventStoreDBPersistentSubscriptionsClient
import io.traxter.eventstoredb.EventStoreDB

interface HasEventStoreClient {
    val client: EventStoreDBClient
    val persistedSubscriptionClient: EventStoreDBPersistentSubscriptionsClient
    val eventStoreDb: EventStoreDB
}
