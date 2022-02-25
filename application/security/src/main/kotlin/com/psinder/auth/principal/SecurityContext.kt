package com.psinder.auth.principal

import arrow.core.getOrElse
import arrow.core.toOption
import com.github.maaxgr.ktor.globalcalldata.callData
import com.psinder.auth.NotAuthenticatedException
import com.psinder.auth.account.AccountContext
import com.psinder.auth.authority.AccountAuthorities
import com.psinder.auth.authority.AuthoritiesProvider
import io.ktor.auth.Principal
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.principal
import mu.KotlinLogging

class SecurityContext(private val authorityProvider: AuthoritiesProvider) : AuthenticatedAccountProvider {
    private val logger = KotlinLogging.logger {}

    override suspend fun currentPrincipal() = when (val principal = callData().call.principal<Principal>()) {
        is JWTPrincipal -> object : AccountContext {
            override val accountId = principal.accountId
            override val role = principal.role
        }
        null -> throw NotAuthenticatedException()
        else -> throw NotImplementedError()
    }

    override suspend fun authorities() = authorityProvider.authorities[currentPrincipal().role].toOption()
        .tapNone { logger.warn("No authorities found for role: ${currentPrincipal().role}") }
        .getOrElse { emptyList() }
        .let { AccountAuthorities(it) }
}
