package com.psinder.auth.authority

import com.psinder.auth.account.AccountContext
import pl.brightinventions.codified.enums.CodifiedEnum

data class AuthorityScope<T>(
    val level: CodifiedEnum<AuthorityLevel, String>,
    val predicates: List<(T, AccountContext) -> Boolean> = emptyList()
)

fun <T> List<AuthorityScope<T>>.getCreateScope() = this.find { it.level.code() == AuthorityLevel.Create.code }
fun <T> List<AuthorityScope<T>>.getUpdateScope() = this.find { it.level.code() == AuthorityLevel.Update.code }
fun <T> List<AuthorityScope<T>>.getReadScope() = this.find { it.level.code() == AuthorityLevel.View.code }
