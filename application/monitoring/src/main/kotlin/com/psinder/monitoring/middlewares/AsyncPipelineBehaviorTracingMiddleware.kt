package com.psinder.monitoring.middlewares

import com.psinder.kediatr.KediatrRequestTypeExtractor
import com.psinder.kediatr.middlewares.AsyncPipelineBehaviorMiddleware
import com.psinder.monitoring.activate
import io.opentracing.Tracer
import mu.KotlinLogging
import org.litote.kmongo.newId

class AsyncPipelineBehaviorTracingMiddleware(private val tracer: Tracer) : AsyncPipelineBehaviorMiddleware {
    override val order = 1

    private val logger = KotlinLogging.logger {}

    override suspend fun <TRequest, TResponse> apply(request: TRequest, act: suspend () -> TResponse): TResponse {
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
