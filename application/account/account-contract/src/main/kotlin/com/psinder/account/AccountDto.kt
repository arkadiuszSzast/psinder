package com.psinder.account

import com.psinder.shared.EmailAddress
import kotlinx.datetime.TimeZone
import org.litote.kmongo.Id
import pl.brightinventions.codified.enums.CodifiedEnum

data class AccountDto(
    val id: Id<AccountDto>,
    val email: EmailAddress,
    val personalData: PersonalData,
    val status: CodifiedEnum<AccountStatus, String>,
    val timeZoneId: TimeZone,
    val lastLoggedInDate: LastLoggedInDate? = null
) {
    companion object
}
