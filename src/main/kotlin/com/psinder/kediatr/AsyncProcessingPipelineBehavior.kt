package com.psinder.kediatr

import com.psinder.monitoring.activate
import com.trendyol.kediatr.AsyncPipelineBehavior
import io.opentracing.Tracer
import mu.KotlinLogging
import org.litote.kmongo.newId

internal class AsyncProcessingPipelineBehavior(private val tracer: Tracer) : AsyncPipelineBehavior {

    private val logger = KotlinLogging.logger {}

    override suspend fun <TRequest, TResponse> process(request: TRequest, act: suspend () -> TResponse): TResponse {
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
