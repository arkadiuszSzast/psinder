package com.psinder.account

import com.psinder.account.activation.AccountActivationError
import com.psinder.account.activation.events.AccountActivatedEvent
import com.psinder.account.activation.events.AccountActivationEvent
import com.psinder.account.activation.events.AccountActivationFailureEvent
import com.psinder.account.events.AccountCreatedEvent
import com.psinder.account.events.AccountLoggedInEvent
import com.psinder.account.events.AccountLoggedInFailureEvent
import com.psinder.account.events.AccountLoggedInSuccessEvent
import com.psinder.auth.account.AccountContext
import com.psinder.auth.account.AccountId
import com.psinder.auth.account.BelongsToAccount
import com.psinder.auth.role.Role
import com.psinder.database.HasId
import com.psinder.shared.EmailAddress
import com.psinder.shared.date.CreatedDate
import com.psinder.shared.password.HashedPassword
import com.psinder.shared.password.RawPassword
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
data class AccountAggregate constructor(
    @SerialName("_id") @Contextual override val id: Id<AccountAggregate>,
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
) : HasId<AccountAggregate>, AccountContext, BelongsToAccount {

    override val accountId: AccountId
        get() = AccountId(id.toString())

    companion object Events
}

fun AccountAggregate.Events.create(
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

fun AccountAggregate.Events.activate(
    accountAggregateId: Id<AccountAggregate>,
    currentAccountStatus: CodifiedEnum<AccountStatus, String>
): AccountActivationEvent {
    return when (currentAccountStatus.knownOrNull()) {
        AccountStatus.Staged -> AccountActivatedEvent(accountAggregateId.cast())
        AccountStatus.Suspended -> AccountActivationFailureEvent(
            accountAggregateId.cast(), AccountActivationError.AccountSuspended.codifiedEnum()
        )
        AccountStatus.Active -> AccountActivationFailureEvent(
            accountAggregateId.cast(), AccountActivationError.AccountActive.codifiedEnum()
        )
        else -> AccountActivationFailureEvent(
            accountAggregateId.cast(), AccountActivationError.AccountStatusUnknown.codifiedEnum()
        )
    }
}

fun AccountAggregate.Events.logIn(
    accountAggregateId: Id<AccountAggregate>,
    accountStatus: CodifiedEnum<AccountStatus, String>,
    accountPassword: HashedPassword,
    logInRequestPassword: RawPassword,
): AccountLoggedInEvent {
    if (!accountPassword.matches(logInRequestPassword)) {
        return AccountLoggedInFailureEvent(accountAggregateId.cast(), LogInFailureError.InvalidPassword.codifiedEnum())
    }

    if (accountStatus.knownOrNull() == AccountStatus.Suspended) {
        return AccountLoggedInFailureEvent(accountAggregateId.cast(), LogInFailureError.AccountSuspended.codifiedEnum())
    }

    if (accountStatus.knownOrNull() == AccountStatus.Staged) {
        return AccountLoggedInFailureEvent(
            accountAggregateId.cast(),
            LogInFailureError.AccountNotActivated.codifiedEnum()
        )
    }

    return AccountLoggedInSuccessEvent(accountAggregateId.cast())
}
