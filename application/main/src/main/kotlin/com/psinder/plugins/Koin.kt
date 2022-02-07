package com.psinder.plugins

import com.psinder.account.accountRepositoriesModule
import com.psinder.account.commands.modules.commandHandlersModule
import com.psinder.kediatr.kediatrModule
import com.psinder.account.commands.modules.queryHandlersModule
import com.psinder.database.kmongoModule
import com.psinder.events.plugins.eventStoreDbKoinModule
import com.psinder.monitoring.middlewares.kediatrMonitoringMiddlewaresModule
import com.psinder.monitoring.plugins.tracerModule
import com.psinder.shared.json.jsonModule
import io.ktor.application.Application
import org.koin.ktor.ext.modules

internal fun Application.configureKoin() {
    modules(
        jsonModule,
        tracerModule,
        kediatrModule,
        queryHandlersModule,
        commandHandlersModule,
        kmongoModule,
        accountRepositoriesModule,
        eventStoreDbKoinModule,
        kediatrMonitoringMiddlewaresModule
    )
}
