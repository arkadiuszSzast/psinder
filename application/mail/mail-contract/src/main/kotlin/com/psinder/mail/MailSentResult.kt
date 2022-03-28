package com.psinder.mail

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

sealed class MailSentResult {
    @Serializable
    data class Success(@Contextual val id: Id<Mail>) : MailSentResult()

    @Serializable
    data class Error(@Contextual val id: Id<Mail>, val cause: MailSendingError) : MailSentResult()
}
