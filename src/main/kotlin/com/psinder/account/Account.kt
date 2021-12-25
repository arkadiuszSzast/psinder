package com.psinder.account

import com.psinder.shared.EmailAddress
import com.psinder.shared.database.HasId
import com.psinder.shared.password.Password
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

internal data class Account(
    @BsonId override val id: Id<Account>,
    val emailAddress: EmailAddress,
    val username: Username,
    val password: Password
) : HasId<Account>
