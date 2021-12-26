package com.psinder.pipelinr

import an.awesome.pipelinr.Command
import an.awesome.pipelinr.CommandHandlers
import an.awesome.pipelinr.Notification
import an.awesome.pipelinr.NotificationHandlers
import an.awesome.pipelinr.ParallelWhenAll
import an.awesome.pipelinr.Pipeline
import an.awesome.pipelinr.Pipelinr
import com.psinder.shared.getAllDistinct
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin
import java.util.concurrent.Executors
import java.util.function.Supplier

private object PipelinrConfig {

    private val pipelinr = Pipelinr()
    val pipeline: Pipeline = pipelinr

    init {
        val commandMiddlewares by lazy { getKoin().getAllDistinct<Command.Middleware>() }
        val notificationMiddlewares by lazy { getKoin().getAllDistinct<Notification.Middleware>() }
        val commandHandlers by lazy { getKoin().getAllDistinct<Command.Handler<*, *>>() }
        val notificationHandlers by lazy { getKoin().getAllDistinct<Notification.Handler<*>>() }

        pipelinr
            .with(Supplier { ParallelWhenAll(Executors.newCachedThreadPool()) })
            .with(Command.Middlewares { commandMiddlewares.stream() })
            .with(Notification.Middlewares { notificationMiddlewares.stream() })
            .with(CommandHandlers { commandHandlers.stream() })
            .with(NotificationHandlers { notificationHandlers.stream() })
    }
}

internal val pipelinrModule = module {
    single { PipelinrConfig.pipeline }
}
