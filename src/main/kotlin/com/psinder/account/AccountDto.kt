package com.psinder.account

import com.psinder.shared.EmailAddress
import com.psinder.shared.LastLoggedInDate
import kotlinx.datetime.TimeZone
import org.litote.kmongo.Id

internal data class AccountDto(
    val id: Id<Account>,
    val email: EmailAddress,
    val personalData: PersonalData,
    val status: AccountStatus,
    val timeZoneId: TimeZone,
    val lastLoggedInUTCDate: LastLoggedInDate? = null
) {
    companion object {
        fun fromAccount(account: Account): AccountDto {
            return AccountDto(
                account.id,
                account.email,
                account.personalData,
                account.status,
                account.timeZoneId,
                account.lastLoggedInUTCDate
            )
        }
    }
}
