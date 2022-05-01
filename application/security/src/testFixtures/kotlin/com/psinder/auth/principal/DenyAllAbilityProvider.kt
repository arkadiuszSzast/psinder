package com.psinder.auth.principal

import com.psinder.auth.AuthorityCheckException
import com.psinder.auth.authority.Deny
import com.psinder.auth.authority.Feature
import kotlin.reflect.KClass

class DenyAllAbilityProvider : AuthorizedAccountAbilityProvider {

    override suspend fun hasAccessTo(feature: Feature) = Deny(AuthorityCheckException("DenyAllAbilityProvider"))

    override suspend fun <T : Any> canCreate(entityRef: KClass<T>) =
        Deny(AuthorityCheckException("DenyAllAbilityProvider"))

    override suspend fun <T : Any> canView(entity: T) = Deny(AuthorityCheckException("DenyAllAbilityProvider"))

    override suspend fun <T : Any> canUpdate(entity: T) = Deny(AuthorityCheckException("DenyAllAbilityProvider"))

    override suspend fun <T : Any> filterCanView(entities: Collection<T>): Collection<T> {
        return entities.filter { canView(it).toBoolean() }
    }

    override suspend fun ensure(): AuthorizedAccountAbilityEnsureProvider {
        return AuthorizedAccountAbilityEnsureProviderImpl(this)
    }
}
