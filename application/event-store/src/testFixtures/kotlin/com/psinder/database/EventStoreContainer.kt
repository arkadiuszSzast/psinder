package com.psinder.database

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait

object EventStoreContainer {
    private val instance by lazy { startEventStoreContainer() }
    val host: String by lazy { instance.host }
    val port: Int by lazy { instance.getMappedPort(1113) }

    private fun startEventStoreContainer() = GenericContainer("eventstore/eventstore:20.10.2-buster-slim")
        .apply {
            addExposedPorts(1113)
            addEnv("EVENTSTORE_CLUSTER_SIZE", "1")
            addEnv("EVENTSTORE_RUN_PROJECTIONS", "All")
            addEnv("EVENTSTORE_START_STANDARD_PROJECTIONS", "true")
            addEnv("EVENTSTORE_EXT_TCP_PORT", "1113")
            addEnv("EVENTSTORE_INSECURE", "true")
            addEnv("EVENTSTORE_ENABLE_EXTERNAL_TCP", "true")
            addEnv("EVENTSTORE_ENABLE_ATOM_PUB_OVER_HTTP", "true")
            setWaitStrategy(Wait.forListeningPort())
            start()
        }
}
