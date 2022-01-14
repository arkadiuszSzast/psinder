package com.psinder.account

import com.psinder.shared.events.DomainEvent
import com.psinder.shared.events.EventFamily
import com.psinder.shared.events.EventType
import com.psinder.shared.password.Password
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

@Serializable
internal data class AccountCreatedEvent(
    @Contextual val accountId: Id<Account>,
    val personalData: PersonalData,
    val password: Password,
    val timeZoneId: TimeZone
) : DomainEvent {

    constructor(account: Account) : this(account.id, account.personalData, account.password, account.timeZoneId)

    override val eventType: EventType = AccountCreatedEvent.eventType
    override val eventFamily: EventFamily = AccountCreatedEvent.eventFamily

    companion object {
        val eventType: EventType = EventType.Created
        val eventFamily: EventFamily = EventFamily.Account
    }
}
