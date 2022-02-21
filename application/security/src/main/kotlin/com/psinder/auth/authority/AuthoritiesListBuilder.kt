package com.psinder.auth.authority

import pl.brightinventions.codified.enums.codifiedEnum

class AuthoritiesListBuilder {
    private val authorities: MutableList<Authority> = mutableListOf()

    fun <T> entityAccess(
        entityRef: Class<T>,
        customize: EntityAccessAuthorityScopeBuilder<T>.() -> Unit
    ) {
        val builder = EntityAccessAuthorityScopeBuilder(entityRef)
        builder.apply(customize)
        authorities.add(builder.build())
    }

    fun featureAccess(feature: Feature) {
        authorities.add(FeatureAccessAuthority(feature.codifiedEnum()))
    }

    fun build() = authorities
}
