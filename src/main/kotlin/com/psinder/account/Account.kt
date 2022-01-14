package com.psinder.account

import com.psinder.shared.EmailAddress
import com.psinder.shared.LastLoggedInDate
import com.psinder.shared.database.HasId
import com.psinder.shared.password.Password
import kotlinx.datetime.TimeZone
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
internal data class Account private constructor(
    @SerialName("_id") override val id: Id<Account>,
    val email: EmailAddress,
    val personalData: PersonalData,
    val password: Password,
    val status: AccountStatus,
    val timeZoneId: TimeZone,
    val lastLoggedInUTCDate: LastLoggedInDate? = null
) : HasId<Account> {
    constructor(
        email: EmailAddress,
        personalData: PersonalData,
        password: Password,
        timeZoneId: TimeZone,
    ) : this(newId(), email, personalData, password, AccountStatus.Staged, timeZoneId)
}
