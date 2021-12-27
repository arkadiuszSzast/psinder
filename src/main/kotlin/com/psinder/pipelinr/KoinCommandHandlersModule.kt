package com.psinder.pipelinr

import an.awesome.pipelinr.Command
import com.psinder.account.commands.handlers.CreateAccountHandler
import com.psinder.account.commands.handlers.LoginAccountHandler
import org.koin.dsl.bind
import org.koin.dsl.module

val pipelinrCommandHandlersModule = module {
    single { CreateAccountHandler() } bind Command.Handler::class
    single { LoginAccountHandler() } bind Command.Handler::class
}
