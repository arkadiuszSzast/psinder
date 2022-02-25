package com.psinder.account

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class LastLoggedInDate(val value: LocalDateTime)
