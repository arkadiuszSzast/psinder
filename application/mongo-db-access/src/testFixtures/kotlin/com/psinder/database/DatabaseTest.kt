package com.psinder.database

import com.psinder.database.transactionally.Transactionally
import com.psinder.test.utils.serializationModule
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.litote.kmongo.coroutine.CoroutineDatabase

abstract class DatabaseTest(vararg neededModules: Module) : KoinTest, HasDatabaseAndTransactionally, DescribeSpec() {
    override val transactionally: Transactionally by inject()
    override val db: CoroutineDatabase by inject()

    override fun beforeEach(testCase: TestCase) {
        runBlocking {
            db.dropAllCollections()
        }
    }

    init {
        startKoin { modules(neededModules.toList().plus(kmongoTestingModule).plus(serializationModule)) }
    }
}

suspend fun CoroutineDatabase.dropAllCollections() = listCollectionNames().forEach { dropCollection(it) }
