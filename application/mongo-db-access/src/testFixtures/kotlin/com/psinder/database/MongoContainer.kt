package com.psinder.database

import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.wait.strategy.Wait

object MongoContainer {
    private val instance by lazy { startMongoContainer() }
    val host: String by lazy { instance.host }
    val port: Int by lazy { instance.getMappedPort(27017) }

    private fun startMongoContainer() = MongoDBContainer("mongo:5.0").apply {
        setWaitStrategy(Wait.forListeningPort())
        start()
    }
}
