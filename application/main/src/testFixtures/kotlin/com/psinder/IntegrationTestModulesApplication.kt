package com.psinder

import com.psinder.auth.authModule
import com.psinder.auth.authorityProviderModule
import com.psinder.aws.localstackTestingModule
import com.psinder.config.JwtAuthConfig
import com.psinder.database.eventStoreTestingModule
import com.psinder.database.kmongoTestingModule
import com.psinder.json.jsonModule
import com.psinder.kediatr.kediatrTestModule
import com.psinder.mail.recordingMailSenderModule
import com.psinder.plugins.configureExceptionsHandling
import com.psinder.plugins.configureGlobalRequestData
import com.psinder.plugins.configureHTTP
import com.psinder.plugins.configureSecurity
import com.psinder.plugins.configureSerialization
import io.ktor.application.Application
import org.koin.ktor.ext.get

fun Application.integrationTestModules() {
    configureGlobalRequestData()
    configureSerialization(get())
    configureExceptionsHandling()
    configureSecurity(JwtAuthConfig)
    configureHTTP()
    configureApplicationStatusRouting()
}

val integrationTestModules = listOf(
    jsonModule,
    kediatrTestModule,
    recordingMailSenderModule,
    kmongoTestingModule,
    eventStoreTestingModule,
    localstackTestingModule,
    authorityProviderModule,
    authModule
)
