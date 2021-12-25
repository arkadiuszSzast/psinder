package com.psinder

import com.psinder.account.configureAccountRouting
import com.psinder.config.DatabaseConfig.connectionString
import com.psinder.config.EventStoreConfig
import com.psinder.config.JwtConfig
import com.psinder.config.SentryConfig
import com.psinder.config.TracingConfig
import com.psinder.plugins.*
import io.ktor.application.*
import io.ktor.network.tls.certificates.*
import io.traxter.eventstoredb.EventStoreDB
import java.io.File

internal fun main(args: Array<String>) {
    generateCertificate(
        file = File("keystore/psinder.link.jks"),
        keyAlias = "psinder.link",
        keyPassword = "foobar",
        jksPassword = "foobar"
    )

    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused")
internal fun Application.main() {
    configureEventStore(EventStoreConfig)
    configureKoin()
    initializeSentry(SentryConfig)
    configureMicrometer(TracingConfig)
    configureOpentracing(TracingConfig)
    configureRouting()
    configureAccountRouting()
    configureExceptionsHandling()
    configureSecurity(JwtConfig)
    configureHTTP()
    configureMonitoring()
    configureSerialization()
}
