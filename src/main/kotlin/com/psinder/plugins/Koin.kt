package com.psinder.plugins

import com.psinder.jackson.jacksonModule
import com.psinder.pipelinr.pipelinrCommandHandlersModule
import com.psinder.pipelinr.pipelinrModule
import com.psinder.pipelinr.pipelinrMiddlewaresModule
import com.psinder.pipelinr.pipelinrNotificationHandlersModule
import com.psinder.shared.database.kmongoModule
import com.psinder.shared.database.repositoriesModule
import io.ktor.application.*
import org.koin.ktor.ext.modules

internal fun Application.configureKoin() {
    modules(
        jacksonModule,
        kmongoModule,
        repositoriesModule,
        pipelinrMiddlewaresModule,
        pipelinrCommandHandlersModule,
        pipelinrNotificationHandlersModule,
        pipelinrModule
    )
}
