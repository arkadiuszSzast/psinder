package com.psinder.mail

import com.psinder.events.AggregateType
import com.psinder.events.DomainEvent
import com.psinder.events.EventName
import com.psinder.events.FullEventType
import com.psinder.shared.EmailAddress
import com.psinder.shared.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import java.util.UUID

@Serializable
data class MailSendingErrorEvent(
    @Contextual val mailId: Id<Mail>,
    val from: EmailAddress,
    val to: EmailAddress,
    val subject: MailSubject,
    val templateId: MailTemplateId,
    val variables: MailVariables,
    val error: MailSendingError
) : DomainEvent, MailSendingEvent() {

    @Serializable(with = UUIDSerializer::class)
    override val eventId: UUID = UUID.randomUUID()

    @Contextual
    override val aggregateId: Id<Mail> = mailId

    override val eventName = MailSendingErrorEvent.eventName

    override val aggregateType: AggregateType = mailAggregateType

    companion object {

        fun create(mail: Mail, error: MailSendingError) =
            MailSendingErrorEvent(mail.id, mail.from, mail.to, mail.subject, mail.templateId, mail.variables, error)

        val eventName: EventName = EventName("sending-error")
        val fullEventType = FullEventType(mailAggregateType, eventName)
    }
}
