package com.psinder.account

import com.psinder.shared.EmailAddress
import com.psinder.shared.password.Password
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

internal data class Account(@BsonId val id: Id<Account>, val emailAddress: EmailAddress, val username: String, val password: Password)