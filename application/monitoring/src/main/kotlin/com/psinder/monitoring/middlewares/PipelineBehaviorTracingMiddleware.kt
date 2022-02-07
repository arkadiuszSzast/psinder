package com.psinder.monitoring.middlewares

import com.psinder.kediatr.KediatrRequestTypeExtractor
import com.psinder.kediatr.middlewares.PipelineBehaviorMiddleware
import com.psinder.monitoring.activate
import io.opentracing.Tracer
import mu.KotlinLogging
import org.litote.kmongo.newId

class PipelineBehaviorTracingMiddleware(private val tracer: Tracer) : PipelineBehaviorMiddleware {
    override val order = 1

    private val logger = KotlinLogging.logger {}

    override fun <TRequest, TResponse> apply(request: TRequest, act: () -> TResponse): TResponse {
        val requestSimpleName = request!!::class.simpleName
        val requestType = KediatrRequestTypeExtractor.extract(request).code()
        val requestId = newId<TRequest>()

        return tracer.buildSpan("$requestType-handler-$requestSimpleName")
            .withTag(requestType, requestSimpleName)
            .start()
            .activate(tracer) {
                logger.debug { "[RequestId ($requestId)] Executing $requestType $requestSimpleName. Payload: [$request]" }
                runCatching { act() }
                    .onFailure {
                        logger.error { "[RequestId ($requestId)] Error while executing $requestType $requestSimpleName. Error message: $it" }
                    }
                    .onSuccess {
                        logger.debug { "[RequestId ($requestId)] $requestType: $requestSimpleName executed successfully" }
                    }
                    .getOrThrow()
            }
    }
}
