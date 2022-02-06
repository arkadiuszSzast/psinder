package com.psinder.kediatr

import com.psinder.kediatr.middlewares.PipelineBehaviorMiddleware
import com.trendyol.kediatr.PipelineBehavior

internal class ProcessingPipelineBehavior(private val middlewares: List<PipelineBehaviorMiddleware>) :
    PipelineBehavior {

    override fun <TRequest, TResponse> process(request: TRequest, act: () -> TResponse): TResponse {
        return if (middlewares.isEmpty()) {
            act()
        } else {
            middlewares
                .sortedBy { it.order }
                .foldRight(act) { curr, next -> { curr.apply(request, next) } }.invoke()
        }
    }
}
