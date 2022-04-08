package com.psinder.account.activation.events

import com.psinder.account.AccountDto
import com.psinder.account.accountAggregateType
import com.psinder.account.activation.commands.AccountActivationError
import com.psinder.events.DomainEvent
import com.psinder.events.EventName
import com.psinder.events.FullEventType
import com.psinder.shared.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import pl.brightinventions.codified.enums.CodifiedEnum
import java.util.UUID

@Serializable
sealed class AccountActivationEvent : DomainEvent

@Serializable
data class AccountActivatedEvent(@Contextual val accountId: Id<AccountDto>) : AccountActivationEvent() {

    @Serializable(with = UUIDSerializer::class)
    override val eventId = UUID.randomUUID()

    @Contextual
    override val aggregateId = accountId

    override val eventName = AccountActivatedEvent.eventName

    override val aggregateType = accountAggregateType

    companion object {
        val eventName: EventName = EventName("activated")
        val fullEventType = FullEventType(accountAggregateType, eventName)
    }
}

@Serializable
data class AccountActivationFailureEvent(
    @Contextual val accountId: Id<AccountDto>,
    @Serializable(with = AccountActivationError.CodifiedSerializer::class)
    val error: CodifiedEnum<AccountActivationError, String>
) : AccountActivationEvent() {

    @Serializable(with = UUIDSerializer::class)
    override val eventId = UUID.randomUUID()

    @Contextual
    override val aggregateId = accountId

    override val eventName = AccountActivationFailureEvent.eventName

    override val aggregateType = accountAggregateType

    companion object {
        val eventName: EventName = EventName("activation-failed")
        val fullEventType = FullEventType(accountAggregateType, eventName)
    }
}
