package com.psinder.kediatr

import com.trendyol.kediatr.AsyncPipelineBehavior
import io.opentracing.Tracer
import mu.KotlinLogging

internal class AsyncProcessingPipelineBehavior(private val tracer: Tracer) : AsyncPipelineBehavior {

    private val logger = KotlinLogging.logger {}

    override suspend fun <TRequest, TResponse> process(request: TRequest, act: suspend () -> TResponse): TResponse {
        return act()
    }
}
