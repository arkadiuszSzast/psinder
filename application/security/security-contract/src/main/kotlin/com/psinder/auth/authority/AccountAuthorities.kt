package com.psinder.auth.authority

import arrow.core.toOption
import kotlin.reflect.KClass

data class AccountAuthorities(private val authorities: List<Authority>) {

    fun findFeature(feature: Feature) =
        authorities.filterIsInstance<FeatureAccessAuthority>()
            .find { it.feature.code == feature.code }
            .toOption()

    fun <T : Any> findCreateScopeFor(entityRef: KClass<out T>) =
        authorities.filterIsInstance<EntityAccessAuthority<T>>()
            .find { it.entityRef == entityRef }?.scopes?.getCreateScope()
            .toOption()

    fun <T : Any> findUpdateScopeFor(entityRef: KClass<out T>) =
        authorities.filterIsInstance<EntityAccessAuthority<T>>()
            .find { it.entityRef == entityRef }?.scopes?.getUpdateScope()
            .toOption()

    fun <T : Any> findViewScopeFor(entityRef: KClass<out T>) =
        authorities.filterIsInstance<EntityAccessAuthority<T>>()
            .find { it.entityRef == entityRef }?.scopes?.getViewScope()
            .toOption()
}
