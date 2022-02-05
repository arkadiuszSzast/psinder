package com.psinder.kediatr.middlewares

interface PipelineBehaviorMiddleware {
    val order: Int

    fun <TRequest, TResponse> apply(request: TRequest, act: () -> TResponse): TResponse
}
