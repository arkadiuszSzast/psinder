package com.psinder.pipelinr

import an.awesome.pipelinr.Notification
import com.psinder.monitoring.activate
import com.zopa.ktor.opentracing.getGlobalTracer
import mu.KotlinLogging

internal class NotificationLoggableMiddleware : Notification.Middleware {
    private val logger = KotlinLogging.logger {}

    override fun <N : Notification> invoke(notification: N, next: Notification.Middleware.Next) {
        var errorThrown = false
        val tracer = getGlobalTracer()
        val baseHandlerClass = next.baseHandlerType

        return tracer.buildSpan("notification-handler-${notification::class.simpleName}")
            .withTag("notification", notification::class.simpleName)
            .withTag("notification-handler", baseHandlerClass.simpleName)
            .start()
            .activate(tracer) {
                logger.debug { "Handling notification: ${notification::class.simpleName} with handler ${baseHandlerClass.simpleName}" }
                try {
                    next.invoke()
                } catch (t: Throwable) {
                    errorThrown = true
                    logger.error { "Notification: ${notification::class.simpleName} handled by: ${baseHandlerClass.simpleName} with error. Error message: ${t.message}" }
                    throw t
                } finally {
                    if (!errorThrown) {
                        logger.debug { "Notification: ${notification::class.simpleName} handled by: ${baseHandlerClass.simpleName} successfully" }
                    }
                }
            }
    }
}
