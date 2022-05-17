package com.psinder.dog

import com.psinder.dog.subscribers.DogProjectionUpdater
import com.psinder.dog.subscribers.dogProjectionUpdater
import io.ktor.application.Application
import org.koin.ktor.ext.get

internal fun Application.configureEventStoreSubscribers() {
    dogProjectionUpdater(get(), get())
}
