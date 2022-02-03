package com.psinder.kediatr

import com.psinder.account.queries.handlers.FindAccountByEmailQueryHandler
import org.koin.dsl.module

val queryHandlersModule = module {
    single { FindAccountByEmailQueryHandler(get()) }
}
