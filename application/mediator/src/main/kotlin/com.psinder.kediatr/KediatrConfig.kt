package com.psinder.kediatr

import com.psinder.shared.getAllDistinct
import com.trendyol.kediatr.koin.KediatrKoin
import org.koin.dsl.module

val kediatrModule = module {
    single { KediatrKoin.getCommandBus() }
    single { ProcessingPipelineBehavior(getAllDistinct()) }
    single { AsyncProcessingPipelineBehavior(getAllDistinct()) }
}
