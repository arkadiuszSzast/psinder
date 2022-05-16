package com.psinder.account

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.psinder.account.config.JwtConfig
import com.psinder.shared.jwt.JwtToken
import java.util.Date

fun createActivationToken(accountId: String): JwtToken {
    val (secret, issuer, expirationTime) = JwtConfig.activateAccount

    return JWT.create()
        .withIssuer(issuer.value)
        .withSubject(accountId)
        .withExpiresAt(Date(System.currentTimeMillis() + expirationTime.millis))
        .sign(Algorithm.HMAC256(secret.value))
        .let { JwtToken.createOrThrow(it) }
}
