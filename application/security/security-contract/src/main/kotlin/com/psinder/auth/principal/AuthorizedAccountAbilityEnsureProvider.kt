package com.psinder.auth.principal

import com.psinder.auth.authority.Feature
import kotlin.reflect.KClass

interface AuthorizedAccountAbilityEnsureProvider {
    suspend fun hasAccessTo(feature: Feature)
    suspend fun <T : Any> canCreate(entityRef: KClass<out T>)
    suspend fun <T : Any> canView(entity: T)
    suspend fun <T : Any> canUpdate(entity: T)
}
