package com.psinder.monitoring.middlewares

import com.psinder.kediatr.middlewares.PipelineBehaviorMiddleware
import io.opentracing.Tracer
import mu.KotlinLogging

class PipelineBehaviorTracingMiddleware(private val tracer: Tracer) : PipelineBehaviorMiddleware {
    override val order = 1

    private val logger = KotlinLogging.logger {}

    override fun <TRequest, TResponse> apply(request: TRequest, act: () -> TResponse): TResponse {
        return TracingPipelineMiddleware.apply(tracer, logger, request) { act() }
    }
}
