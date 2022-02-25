package com.psinder.auth.principal

import com.psinder.auth.AuthorityCheckException
import com.psinder.auth.authority.Feature
import com.psinder.shared.kClassSimpleName
import pl.brightinventions.codified.enums.CodifiedEnum
import kotlin.reflect.KClass

class AuthorizedAccountAbilityEnsureProviderImpl(
    private val acl: AuthorizedAccountAbilityProvider,
    private val authenticatedAccountProvider: AuthenticatedAccountProvider
) : AuthorizedAccountAbilityEnsureProvider {

    override suspend fun hasAccessTo(feature: CodifiedEnum<Feature, String>) {
        if (!acl.hasAccessTo(feature)) {
            val accountId = currentPrincipal().accountId
            throw AuthorityCheckException("Account with id: [$accountId] has no access to ${feature.code()} feature")
        }
    }

    override suspend fun <T : Any> canCreate(entityRef: KClass<out T>) {
        if (!acl.canCreate(entityRef)) {
            val accountId = currentPrincipal().accountId
            throw AuthorityCheckException("Account with id: [$accountId] cannot create ${entityRef.simpleName}")
        }
    }

    override suspend fun <T : Any> canView(entity: T) {
        if (!acl.canView(entity)) {
            val accountId = currentPrincipal().accountId
            throw AuthorityCheckException("Account with id: [$accountId] cannot view ${entity.kClassSimpleName}.")
        }
    }

    override suspend fun <T : Any> canUpdate(entity: T) {
        if (!acl.canUpdate(entity)) {
            val accountId = currentPrincipal().accountId
            throw AuthorityCheckException("Account with id: [$accountId] cannot update ${entity.kClassSimpleName}.")
        }
    }

    private suspend fun currentPrincipal() = authenticatedAccountProvider.currentPrincipal()
}
