package com.psinder.feature.toggle.client.requests

import com.psinder.feature.toggle.RolloutRule
import kotlinx.serialization.Serializable

@Serializable
data class PatchBooleanRolloutRulesRequest(
    val value: List<RolloutRule<Boolean>>,
    val op: String = "replace",
    val path: String = "/rolloutRules"
)
