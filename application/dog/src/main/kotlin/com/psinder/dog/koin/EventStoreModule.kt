package com.psinder.dog.koin

import com.psinder.dog.subscribers.DogProjectionUpdater
import org.koin.dsl.module

val dogEventStoreModule = module {
    single { DogProjectionUpdater(get()) }
}
