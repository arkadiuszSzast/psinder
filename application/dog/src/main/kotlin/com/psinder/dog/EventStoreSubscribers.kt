package com.psinder.dog

import com.psinder.dog.subscribers.dogOverviewProjectionUpdater
import com.psinder.dog.subscribers.dogProfileProjectionUpdater
import io.ktor.application.Application
import org.koin.ktor.ext.get

internal fun Application.configureEventStoreSubscribers() {
    dogProfileProjectionUpdater(get(), get())
    dogOverviewProjectionUpdater(get(), get())
}
