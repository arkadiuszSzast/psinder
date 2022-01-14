package com.psinder.shared

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
internal value class LastLoggedInDate(val value: LocalDateTime)
