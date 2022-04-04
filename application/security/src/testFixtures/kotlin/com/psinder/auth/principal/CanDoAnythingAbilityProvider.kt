package com.psinder.auth.principal

import com.psinder.auth.authority.Allow
import com.psinder.auth.authority.Feature
import java.util.UUID
import kotlin.reflect.KClass

class CanDoAnythingAbilityProvider(private val authenticatedAccountProvider: AuthenticatedAccountProvider) :
    AuthorizedAccountAbilityProvider {

    override suspend fun hasAccessTo(feature: Feature) = Allow(UUID.randomUUID())

    override suspend fun <T : Any> canCreate(entityRef: KClass<T>) = Allow(UUID.randomUUID())

    override suspend fun <T : Any> canView(entity: T) = Allow(UUID.randomUUID())

    override suspend fun <T : Any> canUpdate(entity: T) = Allow(UUID.randomUUID())

    override suspend fun <T : Any> filterCanView(entities: Collection<T>): Collection<T> {
        return entities.filter { canView(it).toBoolean() }
    }

    override suspend fun ensure(): AuthorizedAccountAbilityEnsureProvider {
        return AuthorizedAccountAbilityEnsureProviderImpl(this, authenticatedAccountProvider)
    }
}
