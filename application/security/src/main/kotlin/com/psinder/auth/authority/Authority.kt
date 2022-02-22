package com.psinder.auth.authority

import com.psinder.auth.account.AccountContext
import com.psinder.auth.account.BelongsToAccount
import pl.brightinventions.codified.enums.CodifiedEnum
import kotlin.reflect.KClass

sealed class Authority
data class FeatureAccessAuthority(val feature: CodifiedEnum<Feature, String>) : Authority()
data class EntityAccessAuthority<T : Any>(val entityRef: KClass<out T>, val scopes: List<AuthorityScope<T>>) :
    Authority()

fun <T : Any> List<EntityAccessAuthority<T>>.findCreateScopeFor(entityRef: KClass<out T>) =
    this.find { it.entityRef == entityRef }?.scopes?.getCreateScope()

fun <T : Any> List<EntityAccessAuthority<T>>.findUpdateScopeFor(entityRef: KClass<out T>) =
    this.find { it.entityRef == entityRef }?.scopes?.getUpdateScope()

fun <T : Any> List<EntityAccessAuthority<T>>.findReadScopeFor(entityRef: KClass<out T>) =
    this.find { it.entityRef == entityRef }?.scopes?.getReadScope()

fun <T : BelongsToAccount> ownedPredicate() =
    { entity: T, accountContext: AccountContext -> accountContext.accountId == entity.accountId }
