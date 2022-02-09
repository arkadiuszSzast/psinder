package com.psinder.database.utils

import com.psinder.database.transactionally.Transactionally
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.litote.kmongo.coroutine.CoroutineDatabase

abstract class DatabaseTest(vararg neededModules: Module) : KoinTest, DescribeSpec() {
    val transactionally: Transactionally by inject()
    val db: CoroutineDatabase by inject()

    override fun beforeEach(testCase: TestCase) {
        println("Before")
    }

    init {
        startKoin { modules(*neededModules) }
    }
}