package com.psinder.account.koin

import com.psinder.account.queries.FindAccountByEmailQueryHandler
import com.psinder.account.queries.FindAccountByIdQueryHandler
import org.koin.dsl.module

val accountQueryHandlersModule = module {
    single { FindAccountByEmailQueryHandler(get(), get()) }
    single { FindAccountByIdQueryHandler(get(), get()) }
}
