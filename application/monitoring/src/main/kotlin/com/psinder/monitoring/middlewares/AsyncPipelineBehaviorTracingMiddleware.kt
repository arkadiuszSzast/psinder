package com.psinder.monitoring.middlewares

import com.psinder.kediatr.middlewares.AsyncPipelineBehaviorMiddleware
import io.opentracing.Tracer
import mu.KotlinLogging

class AsyncPipelineBehaviorTracingMiddleware(private val tracer: Tracer) : AsyncPipelineBehaviorMiddleware {
    override val order = 1

    private val logger = KotlinLogging.logger {}

    override suspend fun <TRequest, TResponse> apply(request: TRequest, act: suspend () -> TResponse): TResponse {
        return TracingPipelineMiddleware.apply(tracer, logger, request) { act() }
    }
}
