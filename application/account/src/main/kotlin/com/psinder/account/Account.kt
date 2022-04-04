package com.psinder.account

import com.psinder.auth.account.AccountContext
import com.psinder.auth.account.AccountId
import com.psinder.auth.account.BelongsToAccount
import com.psinder.auth.role.Role
import com.psinder.database.HasId
import com.psinder.shared.EmailAddress
import com.psinder.shared.password.HashedPassword
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.codifiedEnum
import java.time.LocalDateTime.now
import java.time.ZoneId

@Serializable
data class Account constructor(
    @SerialName("_id") @Contextual override val id: Id<Account>,
    val email: EmailAddress,
    val personalData: PersonalData,
    val password: HashedPassword,
    val created: LocalDateTime,
    @Serializable(with = AccountStatus.CodifiedSerializer::class)
    val status: CodifiedEnum<AccountStatus, String>,
    @Serializable(with = Role.CodifiedSerializer::class)
    override var role: CodifiedEnum<Role, String>,
    val timeZoneId: TimeZone,
    val lastLoggedInDate: LastLoggedInDate? = null
) : HasId<Account>, AccountContext, BelongsToAccount {

    companion object {
        fun create(
            email: EmailAddress,
            personalData: PersonalData,
            role: CodifiedEnum<Role, String>,
            password: HashedPassword,
            timeZoneId: TimeZone,
        ): AccountCreatedEvent {
            return AccountCreatedEvent(
                newId(),
                email,
                personalData,
                password,
                now(ZoneId.of("UTC")).toKotlinLocalDateTime(),
                AccountStatus.Staged.codifiedEnum(),
                role,
                timeZoneId
            )
        }
    }

    override val accountId: AccountId
        get() = AccountId(id.toString())
}
