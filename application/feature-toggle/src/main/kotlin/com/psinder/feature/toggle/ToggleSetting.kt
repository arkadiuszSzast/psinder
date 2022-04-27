package com.psinder.feature.toggle

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import pl.brightinventions.codified.enums.CodifiedEnum

@Serializable
data class ToggleSetting(
    val settingId: Long,
    val key: String?,
    val name: String?,
    val hint: String?,
    @Serializable(with = SettingType.CodifiedSerializer::class)
    val settingType: CodifiedEnum<SettingType, String>,
    val order: Long,
    val createdAt: Instant?,
)
