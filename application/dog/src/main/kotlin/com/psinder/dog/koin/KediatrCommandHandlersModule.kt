package com.psinder.dog.koin

import com.psinder.config.JwtAuthConfig
import com.psinder.dog.commands.DislikeDogCommandHandler
import com.psinder.dog.commands.ImpersonateDogCommandHandler
import com.psinder.dog.commands.LikeDogCommandHandler
import com.psinder.dog.commands.RegisterDogCommandHandler
import org.koin.dsl.module

val dogCommandHandlersModule = module {
    single { RegisterDogCommandHandler(get(), get(), get()) }
    single { ImpersonateDogCommandHandler(JwtAuthConfig, get(), get(), get()) }
    single { LikeDogCommandHandler(get(), get()) }
    single { DislikeDogCommandHandler(get(), get()) }
}
