package com.psinder.shared.date

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class CreatedDate(val value: LocalDateTime)
