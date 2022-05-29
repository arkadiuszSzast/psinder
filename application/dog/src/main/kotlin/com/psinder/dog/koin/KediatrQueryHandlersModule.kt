package com.psinder.dog.koin

import com.psinder.dog.queries.FindDogProfileByIdQueryHandler
import com.psinder.dog.queries.FindNotVotedDogsQueryHandler
import org.koin.dsl.module

val dogQueryHandlersModule = module {
    single { FindDogProfileByIdQueryHandler(get(), get()) }
    single { FindNotVotedDogsQueryHandler(get(), get(), get(), get()) }
}
