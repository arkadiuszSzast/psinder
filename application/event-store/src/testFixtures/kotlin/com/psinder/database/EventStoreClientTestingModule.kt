package com.psinder.database

import com.eventstore.dbclient.Endpoint
import com.eventstore.dbclient.EventStoreDBClient
import com.eventstore.dbclient.EventStoreDBClientSettings
import com.eventstore.dbclient.EventStoreDBPersistentSubscriptionsClient
import io.traxter.eventstoredb.EventStoreDB
import io.traxter.eventstoredb.EventStoreDbPlugin
import mu.KotlinLogging
import org.koin.dsl.bind
import org.koin.dsl.module

val eventStoreTestingModule = module {
    single {
        EventStoreDBClient.create(
            EventStoreDBClientSettings.builder()
                .addHost(Endpoint(EventStoreContainer.host, EventStoreContainer.port))
                .buildConnectionSettings()
        )
    }
    single {
        EventStoreDBPersistentSubscriptionsClient.create(
            EventStoreDBClientSettings.builder()
                .addHost(Endpoint(EventStoreContainer.host, EventStoreContainer.port))
                .buildConnectionSettings()
        )
    }
    single {
        EventStoreDbPlugin(
            EventStoreDB.Configuration(
                "esdb://${EventStoreContainer.host}:${EventStoreContainer.port}?tls=false",
                logger = KotlinLogging.logger {}
            )
        )
    } bind EventStoreDB::class
}
