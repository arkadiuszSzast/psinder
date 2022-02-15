package com.psinder.auth

interface AccountIdProvider {
    val accountId: AccountId
}

@JvmInline
value class AccountId(val value: String)
