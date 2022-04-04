package com.psinder.auth.principal

import com.psinder.auth.authority.Allow
import com.psinder.auth.authority.Deny
import com.psinder.auth.authority.Feature
import kotlin.reflect.KClass

class AuthorizedAccountAbilityEnsureProviderImpl(
    private val acl: AuthorizedAccountAbilityProvider,
) : AuthorizedAccountAbilityEnsureProvider {

    override suspend fun hasAccessTo(feature: Feature) {
        when (val decision = acl.hasAccessTo(feature)) {
            is Allow -> Unit
            is Deny -> throw decision.reason
        }
    }

    override suspend fun <T : Any> canCreate(entityRef: KClass<out T>) {
        when (val decision = acl.canCreate(entityRef)) {
            is Allow -> Unit
            is Deny -> throw decision.reason
        }
    }

    override suspend fun <T : Any> canView(entity: T) {
        when (val decision = acl.canView(entity)) {
            is Allow -> Unit
            is Deny -> throw decision.reason
        }
    }

    override suspend fun <T : Any> canUpdate(entity: T) {
        when (val decision = acl.canUpdate(entity)) {
            is Allow -> Unit
            is Deny -> throw decision.reason
        }
    }
}
