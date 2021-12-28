package com.psinder.account

import com.psinder.shared.EmailAddress
import com.psinder.shared.LastLoggedInDate
import com.psinder.shared.database.HasId
import com.psinder.shared.password.Password
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.codifiedEnum

internal data class Account private constructor(
    @BsonId override val id: Id<Account>,
    val email: EmailAddress,
    val personalData: PersonalData,
    val password: Password,
    val status: CodifiedEnum<AccountStatus, String>,
    val lastLoggedInDate: LastLoggedInDate? = null
) : HasId<Account> {
    constructor(
        email: EmailAddress,
        personalData: PersonalData,
        password: Password,
    ) : this(newId(), email, personalData, password, AccountStatus.WAITING_FOR_ACTIVATION.codifiedEnum())
}
