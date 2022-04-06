package com.psinder.database

import com.eventstore.dbclient.EventStoreDBClient
import com.eventstore.dbclient.EventStoreDBPersistentSubscriptionsClient
import com.psinder.test.utils.serializationModule
import io.kotest.common.runBlocking
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.traxter.eventstoredb.EventStoreDB
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.KoinTest
import org.koin.test.inject

abstract class EventStoreTest(vararg neededModules: Module) : KoinTest, HasEventStoreClient, DescribeSpec() {

    override val client: EventStoreDBClient by inject()
    override val persistedSubscriptionClient: EventStoreDBPersistentSubscriptionsClient by inject()
    override val eventStoreDb: EventStoreDB by inject()

    override fun beforeEach(testCase: TestCase) {
        runBlocking {
        }
    }

    override fun afterSpec(spec: Spec) {
        stopKoin()
    }

    init {
        startKoin { modules(neededModules.toList().plus(eventStoreTestingModule).plus(serializationModule)) }
    }
}
