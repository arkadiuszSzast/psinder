package com.psinder.kediatr

import com.psinder.kediatr.middlewares.AsyncPipelineBehaviorMiddleware
import com.psinder.kediatr.middlewares.AsyncPipelineBehaviorTracingMiddleware
import com.psinder.kediatr.middlewares.PipelineBehaviorMiddleware
import com.psinder.kediatr.middlewares.PipelineBehaviorTracingMiddleware
import com.psinder.shared.getAllDistinct
import com.trendyol.kediatr.koin.KediatrKoin
import org.koin.dsl.bind
import org.koin.dsl.module

val kediatrModule = module {
    single { KediatrKoin.getCommandBus() }
    single { ProcessingPipelineBehavior(getAllDistinct()) }
    single { AsyncProcessingPipelineBehavior(getAllDistinct()) }
    single { AsyncPipelineBehaviorTracingMiddleware(get()) } bind AsyncPipelineBehaviorMiddleware::class
    single { PipelineBehaviorTracingMiddleware(get()) } bind PipelineBehaviorMiddleware::class
}
