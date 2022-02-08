package com.psinder.plugins

import com.psinder.account.accountRepositoriesModule
import com.psinder.account.koin.accountCommandHandlersModule
import com.psinder.kediatr.kediatrModule
import com.psinder.account.koin.accountQueryHandlersModule
import com.psinder.database.kmongoModule
import com.psinder.events.plugins.eventStoreDbKoinModule
import com.psinder.json.jsonModule
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
        kmongoModule,
        accountRepositoriesModule,
        eventStoreDbKoinModule,
        kediatrMonitoringMiddlewaresModule
    )
}
