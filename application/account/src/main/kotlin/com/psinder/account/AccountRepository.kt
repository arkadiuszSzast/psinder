package com.psinder.account

import com.psinder.database.MongoRepository
import com.psinder.shared.EmailAddress
import org.litote.kmongo.eq

internal interface AccountRepository : MongoRepository<Account> {

    suspend fun findOneByEmail(email: EmailAddress): Account? {
        return collection.findOne(Account::email eq email)
    }
}
