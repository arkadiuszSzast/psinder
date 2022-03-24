package com.psinder.account

import com.psinder.auth.role.Role
import com.psinder.events.DomainEvent
import com.psinder.events.EventName
import com.psinder.events.FullEventType
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
    @Serializable(with = Role.CodifiedSerializer::class)
    val role: CodifiedEnum<Role, String>,
    val timeZoneId: TimeZone
) : DomainEvent<Account> {

    @Contextual
    override val aggregateId: Id<Account> = accountId

    override val eventName = AccountCreatedEvent.eventName

    fun apply() = Account(accountId, email, personalData, password, status, role, timeZoneId)

    companion object {
        val eventName: EventName = EventName("created")
        val fullEventType = FullEventType(accountAggregateType, eventName)
    }
}
