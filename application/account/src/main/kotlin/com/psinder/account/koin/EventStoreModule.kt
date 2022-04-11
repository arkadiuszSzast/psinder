package com.psinder.account.koin

import com.psinder.account.subscribers.AccountProjectionUpdater
import org.koin.dsl.module

val accountEventStoreModule = module {
    single { AccountProjectionUpdater(get()) }
}
