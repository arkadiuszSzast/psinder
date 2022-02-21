package com.psinder.auth.principal

import com.psinder.auth.authority.Feature
import pl.brightinventions.codified.enums.CodifiedEnum

interface AuthorizedAccountAbilityEnsureProvider {
    suspend fun hasAccessTo(feature: CodifiedEnum<Feature, String>)
    suspend fun <T> canCreate(entityRef: Class<T>)
    suspend fun <T : Any> canView(entity: T)
    suspend fun <T : Any> canUpdate(entity: T)
}
