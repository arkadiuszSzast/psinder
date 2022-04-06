package com.psinder.mail.events

import com.psinder.events.AggregateType
import com.psinder.events.DomainEvent
import com.psinder.events.EventName
import com.psinder.events.FullEventType
import com.psinder.mail.MailDto
import com.psinder.mail.MailSubject
import com.psinder.mail.MailTemplateId
import com.psinder.mail.MailVariables
import com.psinder.mail.mailAggregateType
import com.psinder.shared.EmailAddress
import com.psinder.shared.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import java.util.UUID

@Serializable
data class MailSentSuccessfullyEvent(
    @Contextual val mailId: Id<MailDto>,
    val from: EmailAddress,
    val to: EmailAddress,
    val subject: MailSubject,
    val templateId: MailTemplateId,
    val variables: MailVariables
) : DomainEvent, MailSendingEvent() {

    @Serializable(with = UUIDSerializer::class)
    override val eventId: UUID = UUID.randomUUID()

    @Contextual
    override val aggregateId = mailId

    override val eventName = Companion.eventName

    override val aggregateType: AggregateType = mailAggregateType

    companion object {
        fun create(mail: MailDto) =
            MailSentSuccessfullyEvent(mail.id, mail.from, mail.to, mail.subject, mail.templateId, mail.variables)

        val eventName: EventName = EventName("sent-successfully")
        val fullEventType = FullEventType(mailAggregateType, eventName)
    }
}
