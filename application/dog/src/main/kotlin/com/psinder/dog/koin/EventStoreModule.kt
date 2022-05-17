package com.psinder.dog.koin

import com.psinder.dog.subscribers.DogOverviewProjectionUpdater
import com.psinder.dog.subscribers.DogProfileProjectionUpdater
import org.koin.dsl.module

val dogEventStoreModule = module {
    single { DogProfileProjectionUpdater(get()) }
    single { DogOverviewProjectionUpdater(get()) }
}
