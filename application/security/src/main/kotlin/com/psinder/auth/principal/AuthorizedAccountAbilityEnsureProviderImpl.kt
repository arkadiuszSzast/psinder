package com.psinder.auth.principal

import com.psinder.auth.AuthorityCheckException
import com.psinder.auth.authority.Feature
import pl.brightinventions.codified.enums.CodifiedEnum

class AuthorizedAccountAbilityEnsureProviderImpl(
    private val acl: AuthorizedAccountAbilityProvider,
    private val securityContext: SecurityContext
) : AuthorizedAccountAbilityEnsureProvider {

    override suspend fun hasAccessTo(feature: CodifiedEnum<Feature, String>) {
        if (!acl.hasAccessTo(feature)) {
            val accountId = currentPrincipal().accountId
            throw AuthorityCheckException("Account with id: [$accountId] has no access to ${feature.code()} feature")
        }
    }

    override suspend fun <T> canCreate(entityRef: Class<T>) {
        if (!acl.canCreate(entityRef)) {
            val accountId = currentPrincipal().accountId
            throw AuthorityCheckException("Account with id: [$accountId] cannot create ${entityRef::class.java.name}")
        }
    }

    override suspend fun <T : Any> canView(entity: T) {
        if (!acl.canView(entity)) {
            val accountId = currentPrincipal().accountId
            throw AuthorityCheckException("Account with id: [$accountId] cannot view ${entity::class.java.name}.")
        }
    }

    override suspend fun <T : Any> canUpdate(entity: T) {
        if (!acl.canUpdate(entity)) {
            val accountId = currentPrincipal().accountId
            throw AuthorityCheckException("Account with id: [$accountId] cannot update ${entity::class.java.name}.")
        }
    }

    private suspend fun currentPrincipal() = securityContext.currentPrincipal()
}
