package com.psinder.feature.toggle.client.responses

import com.psinder.feature.toggle.RolloutRule
import com.psinder.feature.toggle.ToggleSetting
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ToggleDetailsResponse<T>(
    val setting: ToggleSetting,
    val updatedAt: Instant?,
    val value: String?,
    val rolloutRules: List<RolloutRule<T>>
)
