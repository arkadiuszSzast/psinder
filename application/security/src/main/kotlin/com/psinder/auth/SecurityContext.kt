package com.psinder.auth

import com.github.maaxgr.ktor.globalcalldata.callData
import io.ktor.auth.Principal
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.principal

class SecurityContext {
    suspend fun currentPrincipal() = when (val principal = callData().call.principal<Principal>()) {
        is JWTPrincipal -> object : AccountContext {
            override val email = principal.email
            override val accountId = principal.accountId
            override val role = principal.role
        }
        null -> throw NotAuthenticatedException()
        else -> throw NotImplementedError()
    }
}
