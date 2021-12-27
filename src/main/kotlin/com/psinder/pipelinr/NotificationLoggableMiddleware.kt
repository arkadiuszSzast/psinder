package com.psinder.pipelinr

import an.awesome.pipelinr.Notification
import com.psinder.monitoring.activate
import com.zopa.ktor.opentracing.getGlobalTracer
import mu.KotlinLogging

internal class NotificationLoggableMiddleware : Notification.Middleware {
    override fun <N : Notification> invoke(notification: N, next: Notification.Middleware.Next) {
        var errorThrown = false
        val tracer = getGlobalTracer()
        val notificationClassName = notification::class.simpleName
        val baseHandlerClassName = next.baseHandlerType.simpleName

        return tracer.buildSpan("notification-handler-$notificationClassName")
            .withTag("notification", notification::class.simpleName)
            .withTag("notification-handler", baseHandlerClassName)
            .start()
            .activate(tracer) {
                logger.debug { "Handling notification: $notificationClassName with handler $baseHandlerClassName" }
                try {
                    next.invoke()
                } catch (t: Throwable) {
                    errorThrown = true
                    logger.error {
                        "Notification [$notificationClassName]: $notification " +
                            "handled by: $baseHandlerClassName with error. Error message: ${t.message}"
                    }
                    throw t
                } finally {
                    if (!errorThrown) {
                        logger.debug { "Notification [$notificationClassName] handled by: $baseHandlerClassName successfully" }
                    }
                }
            }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
