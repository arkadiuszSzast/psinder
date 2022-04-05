package com.psinder.shared.jwt

import arrow.core.Invalid
import arrow.core.Valid
import arrow.core.Validated
import arrow.core.invalid
import arrow.core.valid
import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class JwtToken private constructor(val token: String) {
    companion object {
        fun create(token: String): Validated<JWTDecodeException, JwtToken> {
            return try {
                JWT.decode(token).token.let { JwtToken(it) }.valid()
            } catch (e: JWTDecodeException) {
                e.invalid()
            }
        }

        fun createOrThrow(token: String): JwtToken =
            when (val vToken = create(token)) {
                is Invalid -> throw vToken.value
                is Valid -> vToken.value
            }
    }
}
