package com.psinder.dog.koin

import com.psinder.dog.queries.FindDogProfileByIdQueryHandler
import org.koin.dsl.module

val dogQueryHandlersModule = module {
    single { FindDogProfileByIdQueryHandler(get(), get()) }
}
