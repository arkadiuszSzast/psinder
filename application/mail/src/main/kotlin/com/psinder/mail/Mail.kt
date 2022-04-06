package com.psinder.mail

import com.psinder.mail.events.MailSendingErrorEvent
import com.psinder.mail.events.MailSendingEvent
import com.psinder.mail.events.MailSentSuccessfullyEvent
import com.psinder.shared.EmailAddress
import com.psinder.shared.validation.Validatable
import io.konform.validation.Validation
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

@Serializable
data class Mail(
    @Contextual val id: Id<Mail>,
    val subject: MailSubject,
    val from: EmailAddress,
    val to: EmailAddress,
    val templateId: MailTemplateId,
    val variables: MailVariables
) : Validatable<Mail> {

    suspend fun send(mailSender: MailSender): MailSendingEvent {
        return when (val result = mailSender.send(this)) {
            is MailSentResult.Success -> MailSentSuccessfullyEvent.create(this.toDto())
            is MailSentResult.Error -> MailSendingErrorEvent.create(this.toDto(), result.cause)
        }
    }

    companion object {
        val validator = Validation<Mail> {
            Mail::from {
                run(EmailAddress.validator)
            }
            Mail::to {
                run(EmailAddress.validator)
            }
        }
    }

    override val validator: Validation<Mail>
        get() = Mail.validator
}
