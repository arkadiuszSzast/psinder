package com.psinder.account

import com.psinder.events.DomainEvent
import com.psinder.events.EventFamily
import com.psinder.events.EventType
import com.psinder.shared.EmailAddress
import com.psinder.shared.password.HashedPassword
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import pl.brightinventions.codified.enums.CodifiedEnum

@Serializable
data class AccountCreatedEvent(
    @Contextual val accountId: Id<Account>,
    val email: EmailAddress,
    val personalData: PersonalData,
    val password: HashedPassword,
    @Serializable(with = AccountStatus.CodifiedSerializer::class)
    val status: CodifiedEnum<AccountStatus, String>,
    val timeZoneId: TimeZone
) : DomainEvent {

    override val eventType: EventType = AccountCreatedEvent.eventType
    override val eventFamily: EventFamily = AccountCreatedEvent.eventFamily

    companion object {
        val eventType: EventType = EventType.Created
        val eventFamily: EventFamily = EventFamily.Account
    }
}
