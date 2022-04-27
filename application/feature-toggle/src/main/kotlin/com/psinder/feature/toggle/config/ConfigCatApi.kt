package com.psinder.feature.toggle.config

import com.psinder.feature.toggle.FeatureToggleKey

object ConfigCatApi {
    private const val basePath = "https://api.configcat.com"

    const val sdkKeyHeaderName = "X-CONFIGCAT-SDKKEY"
    fun baseTogglePath(settingKey: FeatureToggleKey) = "$basePath/v1/settings/${settingKey.key}/value"
}
