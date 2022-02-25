package com.psinder.auth

import com.psinder.auth.principal.AuthenticatedAccountProvider
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.auth.principal.AuthorizedAccountAbilityProviderImpl
import com.psinder.auth.principal.SecurityContext
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    single { AuthorizedAccountAbilityProviderImpl(get()) } bind AuthorizedAccountAbilityProvider::class
    single { SecurityContext(get()) } bind AuthenticatedAccountProvider::class
}
