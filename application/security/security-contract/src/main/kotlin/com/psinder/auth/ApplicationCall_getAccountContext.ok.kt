package com.psinder.auth

import com.psinder.auth.account.AccountContext
import com.psinder.auth.principal.accountId
import com.psinder.auth.principal.role
import io.ktor.application.ApplicationCall
import io.ktor.auth.Principal
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.principal
import io.ktor.features.callId

fun ApplicationCall.getAccountContext(): AccountContext =
    when (val principal = principal<Principal>()) {
        is JWTPrincipal -> object : AccountContext {
            override val accountId = principal.accountId
            override val role = principal.role
        }
        null -> AccountContext.unknown
        else -> throw NotImplementedError()
    }
