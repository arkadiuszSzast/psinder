package com.psinder.auth.principal

import com.psinder.auth.account.AccountContext
import com.psinder.auth.account.AccountId
import com.psinder.auth.authority.AccountAuthorities
import com.psinder.auth.authority.AuthoritiesListBuilder
import com.psinder.auth.role.Role
import pl.brightinventions.codified.enums.codifiedEnum

data class FakeAuthenticatedAccountProvider(
    private val principal: AccountContext,
    private val authorities: AccountAuthorities
) : AuthenticatedAccountProvider {
    override suspend fun currentPrincipal() = principal
    override suspend fun authorities() = authorities
}

fun fakeAuthenticatedAccountProvider(
    accountId: AccountId,
    role: Role,
    customize: AuthoritiesListBuilder.() -> Unit
): FakeAuthenticatedAccountProvider {
    val principal = SimpleAccountContext(accountId, role.codifiedEnum())
    val authorities = AuthoritiesListBuilder().apply(customize).build().let { AccountAuthorities(it) }
    return FakeAuthenticatedAccountProvider(principal, authorities)
}

fun unauthenticatedAccountProvider(customize: AuthoritiesListBuilder.() -> Unit): FakeAuthenticatedAccountProvider {
    val authorities = AuthoritiesListBuilder().apply(customize).build().let { AccountAuthorities(it) }
    return FakeAuthenticatedAccountProvider(AccountContext.unknown, authorities)
}
