package com.psinder.account.config

import com.psinder.auth.JwtExpirationTime
import com.psinder.auth.JwtIssuer
import com.psinder.auth.JwtProperties
import com.psinder.auth.JwtSecret
import com.psinder.shared.config.ConfigKey
import com.psinder.shared.config.getProperty

internal object JwtConfig {
    val activateAccount by lazy {
        val secret = getProperty(Keys.activateAccount + Keys.secret)
        val issuer = getProperty(Keys.activateAccount + Keys.issuer)
        val expirationInMillis = getProperty(Keys.activateAccount + Keys.expirationInMillis).toLong()

        JwtProperties(
            JwtSecret(secret),
            JwtIssuer(issuer),
            JwtExpirationTime(expirationInMillis)
        )
    }

    private object Keys {
        val secret = ConfigKey("secret")
        val issuer = ConfigKey("issuer")
        val expirationInMillis = ConfigKey("expirationInMillis")
        val activateAccount = ConfigKey("jwt.activateAccount")
    }
}
