package com.psinder.auth.principal

import com.psinder.auth.account.AccountContext
import com.psinder.auth.authority.AccountAuthorities

interface AuthenticatedAccountProvider {
    suspend fun currentPrincipal(): AccountContext
    suspend fun authorities(): AccountAuthorities
}
