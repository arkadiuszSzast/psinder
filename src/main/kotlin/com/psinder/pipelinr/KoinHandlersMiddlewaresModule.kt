package com.psinder.pipelinr

import an.awesome.pipelinr.Command
import an.awesome.pipelinr.Notification
import org.koin.dsl.bind
import org.koin.dsl.module

internal val pipelinrMiddlewaresModule = module {
    single { CommandLoggableMiddleware() } bind Command.Middleware::class
    single { NotificationLoggableMiddleware() } bind Notification.Middleware::class
}
