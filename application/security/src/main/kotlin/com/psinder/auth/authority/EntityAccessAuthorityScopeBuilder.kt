package com.psinder.auth.authority

import com.psinder.auth.account.AccountContext
import pl.brightinventions.codified.enums.codifiedEnum
import kotlin.reflect.KClass

class EntityAccessAuthorityScopeBuilder<T : Any>(private val entityRef: KClass<out T>) {
    private var scopes: MutableList<AuthorityScope<T>> = mutableListOf()

    fun viewScope(predicates: List<(T, AccountContext) -> Boolean> = emptyList()) {
        scope(AuthorityLevel.View, predicates)
    }

    fun viewScope(predicate: (T, AccountContext) -> Boolean) {
        scope(AuthorityLevel.View, listOf(predicate))
    }

    fun createScope() {
        scope(AuthorityLevel.Create, emptyList())
    }

    fun updateScope(predicates: List<(T, AccountContext) -> Boolean> = emptyList()) {
        scope(AuthorityLevel.Update, predicates)
    }

    fun updateScope(predicate: (T, AccountContext) -> Boolean) {
        scope(AuthorityLevel.Update, listOf(predicate))
    }

    fun allScopes(predicates: List<(T, AccountContext) -> Boolean> = emptyList()) {
        scope(AuthorityLevel.Create)
        scope(AuthorityLevel.View, predicates)
        scope(AuthorityLevel.Update, predicates)
    }

    fun allScopes(predicate: (T, AccountContext) -> Boolean) {
        scope(AuthorityLevel.Create)
        scope(AuthorityLevel.View, listOf(predicate))
        scope(AuthorityLevel.Update, listOf(predicate))
    }

    fun scope(level: AuthorityLevel, predicates: List<(T, AccountContext) -> Boolean> = emptyList()) {
        scopes.add(AuthorityScope(level.codifiedEnum(), predicates))
    }

    fun build() = EntityAccessAuthority(entityRef, scopes)
}
