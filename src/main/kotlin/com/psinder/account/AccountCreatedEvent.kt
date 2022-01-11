package com.psinder.account

import com.psinder.shared.events.DomainEvent
import com.psinder.shared.events.EventFamily
import com.psinder.shared.events.EventType
import com.psinder.shared.password.Password
import org.litote.kmongo.Id
import java.time.ZoneId

internal data class AccountCreatedEvent(
    val accountId: Id<Account>,
    val personalData: PersonalData,
    val password: Password,
    val timeZoneId: ZoneId
) : DomainEvent {

    constructor(account: Account) : this(account.id, account.personalData, account.password, account.timeZoneId)

    override val eventType: EventType = AccountCreatedEvent.eventType
    override val eventFamily: EventFamily = AccountCreatedEvent.eventFamily

    companion object {
        val eventType: EventType = EventType.Created
        val eventFamily: EventFamily = EventFamily.Account
    }
}
