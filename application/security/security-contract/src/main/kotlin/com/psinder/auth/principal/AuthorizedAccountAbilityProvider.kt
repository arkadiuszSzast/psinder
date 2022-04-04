package com.psinder.auth.principal

import com.psinder.auth.authority.Decision
import com.psinder.auth.authority.Feature
import kotlin.reflect.KClass

interface AuthorizedAccountAbilityProvider {
    suspend fun hasAccessTo(feature: Feature): Decision
    suspend fun <T : Any> canCreate(entityRef: KClass<T>): Decision
    suspend fun <T : Any> canView(entity: T): Decision
    suspend fun <T : Any> canUpdate(entity: T): Decision

    suspend fun <T : Any> filterCanView(entities: Collection<T>): Collection<T>
    suspend fun ensure(): AuthorizedAccountAbilityEnsureProvider
}
