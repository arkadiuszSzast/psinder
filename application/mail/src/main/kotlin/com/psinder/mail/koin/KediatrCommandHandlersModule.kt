package com.psinder.mail.koin

import com.psinder.mail.commands.SendMailCommandHandler
import org.koin.dsl.module

val mailCommandHandlersModule = module {
    single { SendMailCommandHandler(get(), get(), get()) }
}
