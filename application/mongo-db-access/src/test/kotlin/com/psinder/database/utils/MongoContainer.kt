package com.psinder.database.utils

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.wait.strategy.Wait

object MongoContainer {
    private val instance by lazy { startMongoContainer() }
    val host by lazy { instance.host }
    val port by lazy { instance.getMappedPort(27017) }

    private fun startMongoContainer() = MongoDBContainer("mongo:5.0").apply {
        setWaitStrategy(Wait.forListeningPort())
        start()
    }
}