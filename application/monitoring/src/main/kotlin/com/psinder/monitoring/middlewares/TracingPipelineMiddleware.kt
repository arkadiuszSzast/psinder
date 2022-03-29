package com.psinder.monitoring.middlewares

import com.psinder.kediatr.KediatrRequestTypeExtractor
import com.psinder.monitoring.activate
import com.psinder.shared.kClassSimpleName
import io.opentracing.Tracer
import mu.KLogger
import org.litote.kmongo.newId

object TracingPipelineMiddleware {
    inline fun <TRequest, TResponse> apply(
        tracer: Tracer,
        logger: KLogger,
        request: TRequest,
        act: () -> TResponse
    ): TResponse {
        val requestSimpleName = request?.kClassSimpleName
        val requestType = KediatrRequestTypeExtractor.extract(request).code()
        val requestId = newId<TRequest>()

        return tracer.buildSpan("$requestType-handler-$requestSimpleName")
            .withTag(requestType, requestSimpleName)
            .start()
            .activate(tracer) {
                logger.debug { "[RequestId ($requestId)] Executing $requestType $requestSimpleName. Payload: [$request]" }
                runCatching { act() }
                    .onFailure {
                        logger.error {
                            "[RequestId ($requestId)] Error while executing $requestType $requestSimpleName. Error message: $it"
                        }
                    }
                    .onSuccess {
                        logger.debug { "[RequestId ($requestId)] $requestType: $requestSimpleName executed successfully. Result: $it" }
                    }
                    .getOrThrow()
            }
    }
}
