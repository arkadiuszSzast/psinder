package com.psinder.kediatr.middlewares

interface AsyncPipelineBehaviorMiddleware {
    val order: Int

    suspend fun <TRequest, TResponse> apply(request: TRequest, act: suspend () -> TResponse): TResponse
}
