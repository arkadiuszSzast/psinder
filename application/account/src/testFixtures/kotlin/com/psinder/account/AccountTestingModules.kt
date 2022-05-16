package com.psinder.account

import com.psinder.account.koin.accountCommandHandlersModule
import com.psinder.account.koin.accountEventStoreModule
import com.psinder.account.koin.accountQueryHandlersModule
import com.psinder.account.koin.accountRepositoriesModule

val accountTestingModules = listOf(
    accountEventStoreModule,
    accountCommandHandlersModule,
    accountQueryHandlersModule,
    accountRepositoriesModule
)
