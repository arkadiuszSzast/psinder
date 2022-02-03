package com.psinder.kediatr

import com.trendyol.kediatr.PipelineBehavior
import io.opentracing.Tracer
import mu.KotlinLogging

internal class ProcessingPipelineBehavior(private val tracer: Tracer) : PipelineBehavior {

    private val logger = KotlinLogging.logger {}

    override fun <TRequest, TResponse> process(request: TRequest, act: () -> TResponse): TResponse {
        return act()
    }
}
