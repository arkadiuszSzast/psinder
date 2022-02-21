package com.psinder.auth

import com.psinder.account.Account
import com.psinder.auth.authority.Authorities
import com.psinder.auth.authority.AuthoritiesProvider
import com.psinder.auth.authority.authoritiesFor
import com.psinder.auth.role.Role
import org.koin.dsl.bind
import org.koin.dsl.module

class StaticAuthorityProvider : AuthoritiesProvider {
    override val authorities: Authorities = Authorities.create(
        authoritiesFor(Role.Admin) {
            entityAccess(Account::class.java) {
                allScopes()
            }
        }
    )
}

val authorityProviderModule = module {
    single { StaticAuthorityProvider() } bind AuthoritiesProvider::class
}
