package com.psinder.account

import com.psinder.database.MongoRepository
import com.psinder.shared.EmailAddress
import org.litote.kmongo.eq

internal interface AccountRepository : MongoRepository<AccountProjection> {

    suspend fun findOneByEmail(email: EmailAddress): AccountProjection? {
        return collection.findOne(AccountProjection::email eq email)
    }
}
