package com.psinder.account

import com.psinder.shared.EmailAddress
import com.psinder.shared.date.CreatedDate
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import pl.brightinventions.codified.enums.CodifiedEnum

@Serializable
data class AccountDto(
    @Contextual val id: Id<AccountDto>,
    val email: EmailAddress,
    val personalData: PersonalData,
    val created: CreatedDate,
    @Serializable(with = AccountStatus.CodifiedSerializer::class)
    val status: CodifiedEnum<AccountStatus, String>,
    val timeZoneId: TimeZone,
    val lastLoggedInDate: LastLoggedInDate? = null
) {
    companion object
}
