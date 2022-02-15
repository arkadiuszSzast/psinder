package com.psinder.auth

import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    single { AuthorizedAccountAbilityProviderImpl(get(), get()) } bind AuthorizedAccountAbilityProvider::class
    single { SecurityContext() }
}
