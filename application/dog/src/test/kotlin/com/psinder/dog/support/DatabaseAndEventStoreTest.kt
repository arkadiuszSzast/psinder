package com.psinder.dog.support

import com.eventstore.dbclient.EventStoreDBClient
import com.eventstore.dbclient.EventStoreDBPersistentSubscriptionsClient
import com.psinder.database.HasDatabaseAndTransactionally
import com.psinder.database.HasEventStoreClient
import com.psinder.database.eventStoreTestingModule
import com.psinder.database.kmongoTestingModule
import com.psinder.database.transactionally.Transactionally
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
import org.litote.kmongo.coroutine.CoroutineDatabase

abstract class DatabaseAndEventStoreTest(private val neededModules: List<Module>) : KoinTest,
    HasDatabaseAndTransactionally, HasEventStoreClient, DescribeSpec() {
    override val transactionally: Transactionally by inject()
    override val db: CoroutineDatabase by inject()
    override val client: EventStoreDBClient by inject()
    override val persistedSubscriptionClient: EventStoreDBPersistentSubscriptionsClient by inject()
    override val eventStoreDb: EventStoreDB by inject()

    override fun beforeEach(testCase: TestCase) {
        runBlocking {
            db.dropAllCollections()
        }
    }

    override fun afterSpec(spec: Spec) {
        stopKoin()
    }

    init {
        startKoin {
            modules(
                neededModules.toList().plus(kmongoTestingModule).plus(eventStoreTestingModule).plus(serializationModule)
            )
        }
    }
}

suspend fun CoroutineDatabase.dropAllCollections() = listCollectionNames().forEach { dropCollection(it) }
