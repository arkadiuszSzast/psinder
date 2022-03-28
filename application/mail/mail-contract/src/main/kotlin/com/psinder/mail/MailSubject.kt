package com.psinder.mail

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class MailSubject(val value: String)
