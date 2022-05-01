package com.psinder.feature.toggle

import com.trendyol.kediatr.Command
import com.trendyol.kediatr.CommandMetadata

data class SetUserFeatureToggleCommand(
    val userId: FeatureToggleUserIdentifier,
    val toggle: FeatureToggleKey,
    val value: Boolean,
    override val metadata: CommandMetadata? = null
) : Command
