package com.psinder.auth

interface AuthorizedAccountAbilityProvider {
    suspend fun <T> canCreate(entityRef: Class<T>)
    suspend fun <T : Any> canView(entity: T)
    suspend fun <T : Any> canUpdate(entity: T)
}
