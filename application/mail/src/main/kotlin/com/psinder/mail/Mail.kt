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

suspend fun Mail.Companion.send(mailSender: MailSender, mailDto: MailDto): MailSendingEvent =
    when (val result = mailSender.send(mailDto)) {
        is MailSentResult.Success -> MailSentSuccessfullyEvent.create(mailDto)
        is MailSentResult.Error -> MailSendingErrorEvent.create(mailDto, result.cause)
    }
