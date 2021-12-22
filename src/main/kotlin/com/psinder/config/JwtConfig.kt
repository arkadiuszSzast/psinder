package com.psinder.config

object JwtConfig {
    val domain = getProperty(Keys.jwt_domain)
    val audience = getProperty(Keys.audience)
    val realm = getProperty(Keys.realm)
    val secret = getProperty(Keys.secret)
    val issuer = getProperty(Keys.issuer)

    private object Keys {
        val jwt_domain = ConfigKey("jwt.domain")
        val audience = ConfigKey("jwt.audience")
        val issuer = ConfigKey("jwt.issuer")
        val realm = ConfigKey("jwt.realm")
        val secret = ConfigKey("jwt.secret")
    }
}