package com.psinder.auth.authority

import com.psinder.auth.account.AccountContext
import pl.brightinventions.codified.enums.codifiedEnum

class EntityAccessAuthorityScopeBuilder<T>(private val entityRef: Class<T>) {
    private var scopes: MutableList<AuthorityScope<T>> = mutableListOf()

    fun readScope(predicates: List<(T, AccountContext) -> Boolean> = emptyList()) {
        scope(AuthorityLevel.Read, predicates)
    }

    fun readScope(predicate: (T, AccountContext) -> Boolean) {
        scope(AuthorityLevel.Read, listOf(predicate))
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
        scope(AuthorityLevel.Read, predicates)
        scope(AuthorityLevel.Update, predicates)
    }

    fun allScopes(predicate: (T, AccountContext) -> Boolean) {
        scope(AuthorityLevel.Create)
        scope(AuthorityLevel.Read, listOf(predicate))
        scope(AuthorityLevel.Update, listOf(predicate))
    }

    fun scope(level: AuthorityLevel, predicates: List<(T, AccountContext) -> Boolean> = emptyList()) {
        scopes.add(AuthorityScope(level.codifiedEnum(), predicates))
    }

    fun build() = EntityAccessAuthority(entityRef, scopes)
}
