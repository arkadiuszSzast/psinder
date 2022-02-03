package com.psinder.kediatr

import com.psinder.account.commands.handlers.CreateAccountHandler
import com.psinder.account.commands.handlers.LoginAccountHandler
import org.koin.dsl.module

val commandHandlersModule = module {
    single { CreateAccountHandler(get(), get()) }
    single { LoginAccountHandler() }
}
