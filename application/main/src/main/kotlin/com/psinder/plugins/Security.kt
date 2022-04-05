package com.psinder.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.psinder.config.JwtAuthConfig
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt

internal fun Application.configureSecurity(jwtConfig: JwtAuthConfig) {

    install(Authentication) {
        jwt {
            realm = jwtConfig.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtConfig.secret))
                    .withAudience(jwtConfig.audience)
                    .withIssuer(jwtConfig.issuer)
                    .build()
            )
            validate { credential ->
                when (credential.payload.audience.contains(jwtConfig.audience)) {
                    true -> JWTPrincipal(credential.payload)
                    else -> null
                }
            }
        }
    }
}
