package com.psinder.auth.principal

import com.psinder.auth.authority.Feature
import pl.brightinventions.codified.enums.CodifiedEnum
import kotlin.reflect.KClass

interface AuthorizedAccountAbilityProvider {
    suspend fun hasAccessTo(feature: CodifiedEnum<Feature, String>): Boolean
    suspend fun <T : Any> canCreate(entityRef: KClass<T>): Boolean
    suspend fun <T : Any> canView(entity: T): Boolean
    suspend fun <T : Any> canUpdate(entity: T): Boolean

    suspend fun <T : Any> filterCanView(entities: Collection<T>): Collection<T>
    suspend fun ensure(): AuthorizedAccountAbilityEnsureProvider
}
