package com.psinder.pipelinr

import an.awesome.pipelinr.Command
import com.psinder.account.commands.handlers.CreateAccountHandler
import com.psinder.account.commands.handlers.LoginAccountHandler
import com.psinder.account.queries.handlers.FindAccountByEmailQueryHandler
import org.koin.dsl.bind
import org.koin.dsl.module

val pipelinrCommandHandlersModule = module {
    single { CreateAccountHandler(get(), get()) } bind Command.Handler::class
    single { LoginAccountHandler() } bind Command.Handler::class
    single { FindAccountByEmailQueryHandler(get()) } bind Command.Handler::class
}
