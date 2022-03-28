package com.psinder.mail

import com.psinder.events.DomainEvent
import com.psinder.events.EventName
import com.psinder.events.FullEventType
import com.psinder.shared.EmailAddress
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

@Serializable
data class MailSentSuccessfullyEvent(
    @Contextual val mailId: Id<Mail>,
    val from: EmailAddress,
    val to: EmailAddress,
    val subject: MailSubject,
    val templateId: MailTemplateId,
    val variables: MailVariables
) : DomainEvent<Mail> {

    @Contextual
    override val aggregateId = mailId
    override val eventName = MailSentSuccessfullyEvent.eventName

    companion object {
        fun create(mail: Mail) =
            MailSentSuccessfullyEvent(mail.id, mail.from, mail.to, mail.subject, mail.templateId, mail.variables)

        val eventName: EventName = EventName("sent-successfully")
        val fullEventType = FullEventType(mailAggregateType, eventName)
    }
}
