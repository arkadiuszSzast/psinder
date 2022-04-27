package com.psinder.feature.toggle

import com.trendyol.kediatr.Query
import kotlinx.serialization.Serializable

data class GetBooleanToggleQuery(val key: FeatureToggleKey) : Query<GetBooleanToggleResult>

sealed class GetBooleanToggleResult {
    @Serializable
    data class Found(val key: FeatureToggleKey, val value: Boolean) : GetBooleanToggleResult()

    @Serializable
    data class NotFound(val key: FeatureToggleKey) : GetBooleanToggleResult()
}
