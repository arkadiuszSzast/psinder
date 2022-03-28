package com.psinder.mail

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class MailSendingError(val cause: String)
