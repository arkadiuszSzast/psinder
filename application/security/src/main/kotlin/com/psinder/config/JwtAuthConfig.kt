package com.psinder.config

import com.psinder.shared.config.ConfigKey
import com.psinder.shared.config.getAsLong
import com.psinder.shared.config.getProperty

object JwtAuthConfig {
    val domain by lazy { getProperty(Keys.jwt_domain) }
    val audience by lazy { getProperty(Keys.audience) }
    val realm by lazy { getProperty(Keys.realm) }
    val secret by lazy { getProperty(Keys.secret) }
    val issuer by lazy { getProperty(Keys.issuer) }
    val expirationInMillis by lazy { getAsLong(Keys.expirationInMillis) }

    private object Keys {
        val jwt_domain = ConfigKey("jwt.domain")
        val audience = ConfigKey("jwt.audience")
        val issuer = ConfigKey("jwt.issuer")
        val realm = ConfigKey("jwt.realm")
        val secret = ConfigKey("jwt.secret")
        val expirationInMillis = ConfigKey("jwt.expirationInMillis")
    }
}
