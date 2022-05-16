package com.psinder.dog.koin

import com.psinder.dog.commands.RegisterDogCommandHandler
import org.koin.dsl.module

val dogCommandHandlersModule = module {
    single { RegisterDogCommandHandler() }
}
