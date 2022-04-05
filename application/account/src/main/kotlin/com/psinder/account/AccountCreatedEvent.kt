package com.psinder.account

import com.psinder.auth.role.Role
import com.psinder.events.AggregateType
import com.psinder.events.DomainEvent
import com.psinder.events.EventName
import com.psinder.events.FullEventType
import com.psinder.shared.EmailAddress
import com.psinder.shared.UUIDSerializer
import com.psinder.shared.date.CreatedDate
import com.psinder.shared.password.HashedPassword
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import pl.brightinventions.codified.enums.CodifiedEnum
import java.util.UUID

@Serializable
data class AccountCreatedEvent(
    @Contextual val accountId: Id<Account>,
    val email: EmailAddress,
    val personalData: PersonalData,
    val password: HashedPassword,
    val created: CreatedDate,
    @Serializable(with = AccountStatus.CodifiedSerializer::class)
    val status: CodifiedEnum<AccountStatus, String>,
    @Serializable(with = Role.CodifiedSerializer::class)
    val role: CodifiedEnum<Role, String>,
    val timeZoneId: TimeZone
) : DomainEvent {

    @Serializable(with = UUIDSerializer::class)
    override val eventId: UUID = UUID.randomUUID()

    @Contextual
    override val aggregateId = accountId

    override val eventName = AccountCreatedEvent.eventName

    override val aggregateType: AggregateType = accountAggregateType

    fun apply() = Account(accountId, email, personalData, password, created, status, role, timeZoneId)

    companion object {
        val eventName: EventName = EventName("created")
        val fullEventType = FullEventType(accountAggregateType, eventName)
    }
}
