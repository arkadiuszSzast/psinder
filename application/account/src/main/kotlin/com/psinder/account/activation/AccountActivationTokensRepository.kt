package com.psinder.account.activation

import arrow.core.Option
import arrow.core.toOption
import com.psinder.auth.account.AccountId
import com.psinder.database.MongoRepository
import org.litote.kmongo.eq

internal interface AccountActivationTokensRepository : MongoRepository<AccountActivationTokens> {

    suspend fun findOneByAccountId(accountId: AccountId): Option<AccountActivationTokens> {
        return collection.findOne(AccountActivationTokens::accountId eq accountId).toOption()
    }
}
