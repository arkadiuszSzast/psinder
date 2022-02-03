package com.psinder

import com.psinder.config.EventStoreConfig
import com.psinder.config.JwtConfig
import com.psinder.config.SentryConfig
import com.psinder.config.TracingConfig
import com.psinder.plugins.configureEventStore
import com.psinder.plugins.configureEventStoreSubscribers
import com.psinder.plugins.configureExceptionsHandling
import com.psinder.plugins.configureHTTP
import com.psinder.plugins.configureKoin
import com.psinder.plugins.configureMicrometer
import com.psinder.plugins.configureMonitoring
import com.psinder.plugins.configureOpentracing
import com.psinder.plugins.configureRouting
import com.psinder.plugins.configureSecurity
import com.psinder.plugins.configureSerialization
import com.psinder.plugins.initializeSentry
import io.ktor.application.Application
import io.ktor.network.tls.certificates.generateCertificate
import org.koin.ktor.ext.get
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
    configureOpentracing(TracingConfig)
    configureKoin()
    configureSerialization(get())
    initializeSentry(SentryConfig)
    configureMicrometer(TracingConfig)
    configureRouting()
    configureExceptionsHandling()
    configureSecurity(JwtConfig)
    configureHTTP()
    configureMonitoring()
    configureEventStoreSubscribers()
}
