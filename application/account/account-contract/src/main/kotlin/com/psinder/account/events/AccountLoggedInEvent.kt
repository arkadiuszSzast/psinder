package com.psinder.account.events

import com.psinder.account.AccountDto
import com.psinder.account.LogInFailureError
import com.psinder.account.accountAggregateType
import com.psinder.events.DomainEvent
import com.psinder.events.EventName
import com.psinder.events.FullEventType
import com.psinder.shared.UUIDSerializer
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import pl.brightinventions.codified.enums.CodifiedEnum
import java.util.UUID

@Serializable
sealed class AccountLoggedInEvent : DomainEvent

@Serializable
data class AccountLoggedInSuccessEvent(
    @Contextual val accountId: Id<AccountDto>,
    val attemptDate: Instant = Clock.System.now()
) : AccountLoggedInEvent() {
    @Serializable(with = UUIDSerializer::class)
    override val eventId: UUID = UUID.randomUUID()

    @Contextual
    override val aggregateId = accountId

    override val eventName = Companion.eventName

    override val aggregateType = accountAggregateType

    companion object {
        val eventName: EventName = EventName("logged-in-successfully")
        val fullEventType = FullEventType(accountAggregateType, eventName)
    }
}

@Serializable
data class AccountLoggedInFailureEvent(
    @Contextual val accountId: Id<AccountDto>,
    @Serializable(with = LogInFailureError.CodifiedSerializer::class)
    val error: CodifiedEnum<LogInFailureError, String>,
    val attemptDate: Instant = Clock.System.now()
) : AccountLoggedInEvent() {
    @Serializable(with = UUIDSerializer::class)
    override val eventId: UUID = UUID.randomUUID()

    @Contextual
    override val aggregateId = accountId

    override val eventName = Companion.eventName

    override val aggregateType = accountAggregateType

    companion object {
        val eventName: EventName = EventName("login-failure")
        val fullEventType = FullEventType(accountAggregateType, eventName)
    }
}
