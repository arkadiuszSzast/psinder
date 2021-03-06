package com.psinder

import com.psinder.config.JwtAuthConfig
import com.psinder.plugins.configureExceptionsHandling
import com.psinder.plugins.configureGlobalRequestData
import com.psinder.plugins.configureHTTP
import com.psinder.plugins.configureKoin
import com.psinder.plugins.configureSecurity
import com.psinder.plugins.configureSerialization
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
    configureGlobalRequestData()
    configureKoin()
    configureSerialization(get())
    configureExceptionsHandling()
    configureSecurity(JwtAuthConfig)
    configureHTTP()
    configureApplicationStatusRouting()
}
