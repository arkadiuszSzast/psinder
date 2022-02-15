package com.psinder.auth

import com.auth0.jwt.exceptions.JWTDecodeException
import com.psinder.shared.EmailAddress
import io.ktor.auth.jwt.JWTPrincipal
import pl.brightinventions.codified.enums.codifiedEnum

val JWTPrincipal.accountId
    get() = this["accountId"]?.let { AccountId(it) } ?: throw JWTDecodeException("Claim accountId is missing")

val JWTPrincipal.role
    get() = this["role"]?.codifiedEnum<Role>() ?: throw JWTDecodeException("Claim role is missing")

val JWTPrincipal.email
    get() = this["email"]?.let { EmailAddress.create(it) } ?: throw JWTDecodeException("Claim email is missing")
