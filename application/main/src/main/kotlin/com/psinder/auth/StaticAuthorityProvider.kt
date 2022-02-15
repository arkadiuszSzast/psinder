package com.psinder.auth

import com.psinder.account.Account
import org.koin.dsl.bind
import org.koin.dsl.module
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.codifiedEnum

class StaticAuthorityProvider : AuthoritiesProvider {
    override val authorities: Map<CodifiedEnum<Role, String>, List<Authority>> = mapOf(
        Role.Admin.codifiedEnum() to listOf(
            Authority(
                Account::class.java,
                listOf(AuthorityLevel.Read, AuthorityLevel.Update, AuthorityLevel.Create)
            )
        )
    )
}

val authorityProviderModule = module {
    single { StaticAuthorityProvider() } bind AuthoritiesProvider::class
}
