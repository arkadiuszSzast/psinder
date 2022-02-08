package com.psinder.account.koin

import com.psinder.account.commands.CreateAccountHandler
import com.psinder.account.commands.LoginAccountHandler
import org.koin.dsl.module

val accountCommandHandlersModule = module {
    single { CreateAccountHandler(get(), get()) }
    single { LoginAccountHandler() }
}
