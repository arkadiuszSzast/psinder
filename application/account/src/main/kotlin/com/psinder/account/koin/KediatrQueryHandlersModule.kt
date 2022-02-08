package com.psinder.account.koin

import com.psinder.account.queries.FindAccountByEmailQueryHandler
import org.koin.dsl.module

val accountQueryHandlersModule = module {
    single { FindAccountByEmailQueryHandler(get()) }
}
