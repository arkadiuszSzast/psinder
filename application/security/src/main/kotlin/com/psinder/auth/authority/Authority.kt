package com.psinder.auth.authority

import com.psinder.auth.account.AccountContext
import com.psinder.auth.account.BelongsToAccount
import pl.brightinventions.codified.enums.CodifiedEnum

sealed class Authority
data class FeatureAccessAuthority(val feature: CodifiedEnum<Feature, String>) : Authority()
data class EntityAccessAuthority<T>(val entityRef: Class<T>, val scopes: List<AuthorityScope<T>>) : Authority()

fun <T> List<EntityAccessAuthority<T>>.findCreateScopeFor(entityRef: Class<T>) =
    this.find { it.entityRef == entityRef }?.scopes?.getCreateScope()

fun <T> List<EntityAccessAuthority<T>>.findUpdateScopeFor(entityRef: Class<T>) =
    this.find { it.entityRef == entityRef }?.scopes?.getUpdateScope()

fun <T> List<EntityAccessAuthority<T>>.findReadScopeFor(entityRef: Class<T>) =
    this.find { it.entityRef == entityRef }?.scopes?.getReadScope()

fun <T : BelongsToAccount> ownedPredicate() =
    { entity: T, accountContext: AccountContext -> accountContext.accountId == entity.accountId }
