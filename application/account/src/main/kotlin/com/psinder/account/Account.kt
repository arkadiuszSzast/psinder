package com.psinder.account

import com.psinder.account.activation.commands.AccountActivationError
import com.psinder.account.activation.events.AccountActivatedEvent
import com.psinder.account.activation.events.AccountActivationEvent
import com.psinder.account.activation.events.AccountActivationFailureEvent
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
    val created: CreatedDate,
    @Serializable(with = AccountStatus.CodifiedSerializer::class)
    val status: CodifiedEnum<AccountStatus, String>,
    @Serializable(with = Role.CodifiedSerializer::class)
    override var role: CodifiedEnum<Role, String>,
    val timeZoneId: TimeZone,
    val lastLoggedInDate: LastLoggedInDate? = null
) : HasId<Account>, AccountContext, BelongsToAccount {

    override val accountId: AccountId
        get() = AccountId(id.toString())

    companion object {
        fun create(
            email: EmailAddress,
            personalData: PersonalData,
            role: CodifiedEnum<Role, String>,
            password: HashedPassword,
            timeZoneId: TimeZone,
        ) = AccountCreatedEvent(
            newId(),
            email,
            personalData,
            password,
            CreatedDate(now(ZoneId.of("UTC")).toKotlinLocalDateTime()),
            AccountStatus.Staged.codifiedEnum(),
            role,
            timeZoneId
        )

        fun applyCreatedEvent(event: AccountCreatedEvent) = Account(
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

fun Account.Companion.activate(
    accountId: Id<Account>,
    currentAccountStatus: CodifiedEnum<AccountStatus, String>
): AccountActivationEvent {
    return when (currentAccountStatus.knownOrNull()) {
        AccountStatus.Staged -> AccountActivatedEvent(accountId.cast())
        AccountStatus.Suspended -> AccountActivationFailureEvent(
            accountId.cast(), AccountActivationError.AccountSuspended.codifiedEnum()
        )
        AccountStatus.Active -> AccountActivationFailureEvent(
            accountId.cast(), AccountActivationError.AccountActive.codifiedEnum()
        )
        else -> AccountActivationFailureEvent(
            accountId.cast(), AccountActivationError.AccountStatusUnknown.codifiedEnum()
        )
    }
}
