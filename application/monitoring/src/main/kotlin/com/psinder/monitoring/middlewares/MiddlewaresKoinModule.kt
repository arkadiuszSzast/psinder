package com.psinder.monitoring.middlewares

import com.psinder.kediatr.middlewares.AsyncPipelineBehaviorMiddleware
import com.psinder.kediatr.middlewares.PipelineBehaviorMiddleware
import org.koin.dsl.bind
import org.koin.dsl.module

val kediatrMonitoringMiddlewaresModule = module {
    single { AsyncPipelineBehaviorTracingMiddleware(get()) } bind AsyncPipelineBehaviorMiddleware::class
    single { PipelineBehaviorTracingMiddleware(get()) } bind PipelineBehaviorMiddleware::class
}
