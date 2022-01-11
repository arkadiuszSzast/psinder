package com.psinder.account

import com.psinder.shared.EmailAddress
import com.psinder.shared.database.MongoRepository

internal interface AccountRepository : MongoRepository<Account> {

    suspend fun findOneByEmail(email: EmailAddress): Account? {
        return collection.findOne("{email: '${email.value}'}")
    }
}
