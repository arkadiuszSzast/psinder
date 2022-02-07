package com.psinder.kediatr

import com.psinder.kediatr.middlewares.AsyncPipelineBehaviorMiddleware
import com.trendyol.kediatr.AsyncPipelineBehavior

class AsyncProcessingPipelineBehavior(private val middlewares: List<AsyncPipelineBehaviorMiddleware>) :
    AsyncPipelineBehavior {

    override suspend fun <TRequest, TResponse> process(request: TRequest, act: suspend () -> TResponse): TResponse {
        return if (middlewares.isEmpty()) {
            act()
        } else {
            middlewares
                .sortedBy { it.order }
                .foldRight(act) { curr, next -> { curr.apply(request, next) } }.invoke()
        }
    }
}
