package com.psinder.auth.authority

import kotlin.reflect.KClass

class AuthoritiesListBuilder {
    private val authorities: MutableList<Authority> = mutableListOf()

    fun <T : Any> entityAccess(
        entityRef: KClass<out T>,
        customize: EntityAccessAuthorityScopeBuilder<T>.() -> Unit
    ) {
        val builder = EntityAccessAuthorityScopeBuilder(entityRef)
        builder.apply(customize)
        authorities.add(builder.build())
    }

    fun featureAccess(feature: Feature) {
        authorities.add(FeatureAccessAuthority(feature))
    }

    fun build() = authorities.toList()
}
