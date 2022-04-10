package com.psinder.account

import com.psinder.account.events.AccountCreatedEvent
import com.psinder.auth.account.AccountContext
import com.psinder.auth.account.AccountId
import com.psinder.auth.account.BelongsToAccount
import com.psinder.auth.role.Role
import com.psinder.database.HasId
import com.psinder.shared.EmailAddress
import com.psinder.shared.date.CreatedDate
import com.psinder.shared.password.HashedPassword
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import pl.brightinventions.codified.enums.CodifiedEnum

@Serializable
data class AccountProjection constructor(
    @SerialName("_id") @Contextual override val id: Id<AccountProjection>,
    val email: EmailAddress,
    val personalData: PersonalData,
    val password: HashedPassword,
    val created: CreatedDate,
    @Serializable(with = AccountStatus.CodifiedSerializer::class)
    val status: CodifiedEnum<AccountStatus, String>,
    @Serializable(with = Role.CodifiedSerializer::class)
    override var role: CodifiedEnum<Role, String>,
    val timeZoneId: TimeZone,
    val lastLoggedInDate: LastLoggedInDate? = null
) : HasId<AccountProjection>, AccountContext, BelongsToAccount {

    override val accountId: AccountId
        get() = AccountId(id.toString())

    companion object {

        fun applyCreatedEvent(event: AccountCreatedEvent) = AccountProjection(
            event.accountId.cast(),
            event.email,
            event.personalData,
            event.password,
            event.created,
            event.status,
            event.role,
            event.timeZoneId
        )
    }
}
