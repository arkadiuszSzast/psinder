package com.psinder.account.koin

import com.psinder.account.activation.commands.GenerateAccountActivateTokenHandler
import com.psinder.account.commands.CreateAccountHandler
import com.psinder.account.commands.LoginAccountHandler
import com.psinder.account.config.JwtConfig
import org.koin.dsl.module

val accountCommandHandlersModule = module {
    single { CreateAccountHandler(get(), get(), get()) }
    single { LoginAccountHandler() }
    single { GenerateAccountActivateTokenHandler(JwtConfig, get(), get()) }
}
