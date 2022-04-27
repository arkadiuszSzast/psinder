package com.psinder.account.activation.events

import com.psinder.account.AccountDto
import com.psinder.account.accountAggregateType
import com.psinder.events.DomainEvent
import com.psinder.events.EventName
import com.psinder.events.FullEventType
import com.psinder.shared.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import java.util.UUID

@Serializable
data class AccountActivationMailSentEvent(
    @Contextual val accountId: Id<AccountDto>,
    val activationLink: String
) : DomainEvent {
    @Serializable(with = UUIDSerializer::class)
    override val eventId: UUID = UUID.randomUUID()

    @Contextual
    override val aggregateId = accountId

    override val eventName = AccountActivationMailSentEvent.eventName

    override val aggregateType = accountAggregateType

    companion object {
        val eventName: EventName = EventName("activation-mail-sent")
        val fullEventType = FullEventType(accountAggregateType, eventName)
    }
}
