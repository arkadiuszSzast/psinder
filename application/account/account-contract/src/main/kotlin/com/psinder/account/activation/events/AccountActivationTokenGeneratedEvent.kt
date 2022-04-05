package com.psinder.account.activation.events

import com.psinder.account.AccountDto
import com.psinder.account.accountAggregateType
import com.psinder.events.DomainEvent
import com.psinder.events.EventName
import com.psinder.events.FullEventType
import com.psinder.shared.UUIDSerializer
import com.psinder.shared.jwt.JwtToken
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import java.util.UUID

@Serializable
data class AccountActivationTokenGeneratedEvent(
    @Contextual val accountId: Id<AccountDto>,
    val token: JwtToken
) : DomainEvent {
    @Serializable(with = UUIDSerializer::class)
    override val eventId: UUID = UUID.randomUUID()

    @Contextual
    override val aggregateId = accountId

    override val eventName = AccountActivationTokenGeneratedEvent.eventName

    override val aggregateType = accountAggregateType

    companion object {
        val eventName: EventName = EventName("activate-token-generated")
        val fullEventType = FullEventType(accountAggregateType, eventName)
    }
}
