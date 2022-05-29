package com.psinder.dog

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.psinder.auth.account.AccountId
import com.psinder.auth.account.DogId
import com.psinder.config.JwtAuthConfig
import io.ktor.application.ApplicationCall

fun ApplicationCall.getDogContext(): DogContext {
    val dogAuthHeader = this.request.headers["Impersonate"]
    val decodedHeader = JWT
        .require(Algorithm.HMAC256(JwtAuthConfig.secret))
        .withAudience(JwtAuthConfig.audience)
        .withIssuer(JwtAuthConfig.issuer)
        .build()
        .verify(dogAuthHeader)
    val dogId = DogId(decodedHeader.getClaim("dogId").asString())
    val accountId = AccountId(decodedHeader.getClaim("accountId").asString())

    return object : DogContext {
        override val dogId: DogId = dogId
        override val accountId: AccountId = accountId
    }
}
