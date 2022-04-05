package com.psinder.kediatr

import com.trendyol.kediatr.koin.KediatrKoin
import org.koin.dsl.module

val kediatrTestModule = module {
    single { KediatrKoin.getCommandBus("com.psinder") }
    single { ProcessingPipelineBehavior(emptyList()) }
    single { AsyncProcessingPipelineBehavior(emptyList()) }
}
