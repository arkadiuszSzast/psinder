package com.psinder.mail

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

sealed class MailSentResult {
    @Serializable
    data class Success(@Contextual val mailId: Id<MailDto>) : MailSentResult()

    @Serializable
    data class Error(@Contextual val mailId: Id<MailDto>, val cause: MailSendingError) : MailSentResult()
}
