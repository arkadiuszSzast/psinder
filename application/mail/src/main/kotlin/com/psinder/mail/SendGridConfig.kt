package com.psinder.mail

import com.psinder.shared.config.ConfigKey
import com.psinder.shared.config.getProperty

internal object SendGridConfig {
    val apiKey by lazy { getProperty(Keys.apiKey) }

    private object Keys {
        val apiKey = ConfigKey("mail.sendgridApiKey")
    }
}