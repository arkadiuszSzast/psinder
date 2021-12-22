package com.psinder

import com.psinder.account.configureAccountRouting
import com.psinder.plugins.*
import io.ktor.application.*
import io.ktor.network.tls.certificates.*
import java.io.File

fun main(args: Array<String>): Unit {
    val keyStoreFile = File("keystore/psinder.link.jks")
    val keystore = generateCertificate(
        file = keyStoreFile,
        keyAlias = "psinder.link",
        keyPassword = "foobar",
        jksPassword = "foobar"
    )

    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    initializeSentry()
    configureMicrometer()
    configureOpentracing()
    configureRouting()
    configureAccountRouting()
    configureExceptionsHandling()
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
}
