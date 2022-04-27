package com.psinder.feature.toggle.client

import com.psinder.feature.toggle.FeatureToggleKey
import com.psinder.feature.toggle.FeatureToggleUserIdentifier

interface ConfigCatSetToggleClient {
    suspend fun setToggleByUserId(userId: FeatureToggleUserIdentifier, toggle: FeatureToggleKey, value: Boolean)
}
