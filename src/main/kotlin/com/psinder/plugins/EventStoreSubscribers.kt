package com.psinder.plugins

import com.psinder.account.subscribers.accountProjectionUpdater
import io.ktor.application.Application
import org.koin.ktor.ext.get

internal fun Application.configureEventStoreSubscribers() {
    accountProjectionUpdater(get())
}
