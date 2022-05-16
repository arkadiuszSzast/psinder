package com.psinder.account.koin

import com.psinder.account.activation.commands.ActivateAccountCommandHandler
import com.psinder.account.activation.commands.GenerateAccountActivationLinkHandler
import com.psinder.account.activation.commands.GenerateAccountActivationTokenHandler
import com.psinder.account.commands.CreateAccountHandler
import com.psinder.account.commands.LoginAccountHandler
import com.psinder.account.config.JwtConfig
import com.psinder.config.JwtAuthConfig
import com.psinder.shared.config.ApplicationConfig
import org.koin.dsl.module

val accountCommandHandlersModule = module {
    single { CreateAccountHandler(get(), get(), get()) }
    single { LoginAccountHandler(JwtAuthConfig, get(), get()) }
    single { GenerateAccountActivationTokenHandler(JwtConfig, get()) }
    single { GenerateAccountActivationLinkHandler(ApplicationConfig, get(), get()) }
    single { ActivateAccountCommandHandler(JwtConfig, get(), get(), get()) }
}
