package com.psinder.account

import com.psinder.shared.EmailAddress
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import pl.brightinventions.codified.enums.CodifiedEnum

@Serializable
data class AccountDto(
    val id: Id<AccountDto>,
    val email: EmailAddress,
    val personalData: PersonalData,
    @Serializable(with = AccountStatus.CodifiedSerializer::class)
    val status: CodifiedEnum<AccountStatus, String>,
    val timeZoneId: TimeZone,
    val lastLoggedInDate: LastLoggedInDate? = null
) {
    companion object
}
