package com.psinder.aws.config

import com.psinder.shared.config.ConfigKey
import com.psinder.shared.config.getPropertyAsBoolean

internal object AWSConfig {

    val useLocalstack by lazy { getPropertyAsBoolean(Keys.useLocalstack) }

    private object Keys {
        val useLocalstack = ConfigKey("aws.useLocalstack")
    }
}
