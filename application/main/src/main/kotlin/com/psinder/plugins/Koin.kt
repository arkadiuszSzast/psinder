package com.psinder.plugins

import com.psinder.account.koin.accountCommandHandlersModule
import com.psinder.account.koin.accountEventStoreModule
import com.psinder.account.koin.accountQueryHandlersModule
import com.psinder.account.koin.accountRepositoriesModule
import com.psinder.auth.authModule
import com.psinder.auth.authorityProviderModule
import com.psinder.aws.koin.awsKoinModule
import com.psinder.database.kmongoModule
import com.psinder.dog.koin.dogCommandHandlersModule
import com.psinder.dog.koin.dogEventStoreModule
import com.psinder.dog.koin.dogQueryHandlersModule
import com.psinder.dog.koin.dogRepositoriesModule
import com.psinder.events.plugins.eventStoreDbKoinModule
import com.psinder.feature.toggle.configCatModule
import com.psinder.feature.toggle.koin.featureTogglesCommandHandlersModule
import com.psinder.feature.toggle.koin.featureTogglesQueryHandlersModule
import com.psinder.file.storage.koin.fileStorageCommandHandlersModule
import com.psinder.json.jsonModule
import com.psinder.kediatr.kediatrModule
import com.psinder.mail.koin.mailCommandHandlersModule
import com.psinder.mail.sendGridModule
import com.psinder.monitoring.middlewares.kediatrMonitoringMiddlewaresModule
import com.psinder.monitoring.plugins.tracerModule
import io.ktor.application.Application
import org.koin.ktor.ext.modules

internal fun Application.configureKoin() {
    modules(
        jsonModule,
        tracerModule,
        kediatrModule,
        accountQueryHandlersModule,
        accountCommandHandlersModule,
        accountRepositoriesModule,
        accountEventStoreModule,
        dogCommandHandlersModule,
        dogQueryHandlersModule,
        dogRepositoriesModule,
        dogEventStoreModule,
        kmongoModule,
        sendGridModule,
        mailCommandHandlersModule,
        authModule,
        authorityProviderModule,
        eventStoreDbKoinModule,
        kediatrMonitoringMiddlewaresModule,
        configCatModule,
        featureTogglesQueryHandlersModule,
        featureTogglesCommandHandlersModule,
        awsKoinModule,
        fileStorageCommandHandlersModule
    )
}
