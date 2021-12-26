package com.psinder.pipelinr

import an.awesome.pipelinr.Command
import com.psinder.monitoring.activate
import com.zopa.ktor.opentracing.getGlobalTracer
import mu.KotlinLogging

internal class CommandLoggableMiddleware : Command.Middleware {
    private val logger = KotlinLogging.logger {}

    override fun <R : Any, C : Command<R>> invoke(command: C, next: Command.Middleware.Next<R>): R {
        var errorThrown = false
        val tracer = getGlobalTracer()

        return tracer.buildSpan("command-handler-${command::class.simpleName}")
            .withTag("command", command::class.simpleName)
            .start()
            .activate(tracer) {
                try {
                    logger.debug { "Invoking command ${command::class.simpleName}" }
                    next.invoke()
                } catch (t: Throwable) {
                    errorThrown = true
                    logger.error { "Error while handling command ${command::class.simpleName}. Error message: ${t.message}" }
                    throw t
                } finally {
                    if (!errorThrown) {
                        logger.debug { "Command: ${command::class.simpleName} handled successfully" }
                    }
                }
            }
    }
}
