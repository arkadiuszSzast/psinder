package com.psinder.plugins

import com.psinder.kediatr.commandHandlersModule
import com.psinder.kediatr.kediatrModule
import com.psinder.kediatr.queryHandlersModule
import com.psinder.shared.database.kmongoModule
import com.psinder.shared.database.repositoriesModule
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
        repositoriesModule,
        eventStoreDbKoinModule
    )
}
