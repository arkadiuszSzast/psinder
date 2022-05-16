package com.psinder.auth.principal

import arrow.core.getOrElse
import arrow.core.toOption
import com.psinder.auth.account.AccountContext
import com.psinder.auth.authority.AccountAuthorities
import com.psinder.auth.authority.AuthoritiesProvider
import com.psinder.auth.getAccountContext
import com.szastarek.ktor.globalrequestdata.requestData
import io.ktor.auth.Principal
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.principal
import mu.KotlinLogging

class SecurityContext(private val authorityProvider: AuthoritiesProvider) : AuthenticatedAccountProvider {
    private val logger = KotlinLogging.logger {}

    override suspend fun currentPrincipal() = requestData()?.getAccountContext() ?: AccountContext.unknown

    override suspend fun authorities() = authorityProvider.authorities[currentPrincipal().role].toOption()
        .tapNone { logger.warn("No authorities found for role: ${currentPrincipal().role}") }
        .getOrElse { emptyList() }
        .let { AccountAuthorities(it) }
}
