package com.psinder.auth.authority

import com.psinder.auth.account.AccountContext
import com.psinder.auth.account.BelongsToAccount
import kotlin.reflect.KClass

sealed class Authority
data class FeatureAccessAuthority(val feature: Feature) : Authority()
data class EntityAccessAuthority<T : Any>(val entityRef: KClass<out T>, val scopes: List<AuthorityScope<T>>) :
    Authority()

fun <T : BelongsToAccount> ownedPredicate() =
    { entity: T, accountContext: AccountContext -> accountContext.accountId == entity.accountId }
