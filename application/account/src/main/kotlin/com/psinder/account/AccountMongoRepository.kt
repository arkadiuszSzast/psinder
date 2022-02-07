package com.psinder.account

import org.litote.kmongo.coroutine.CoroutineCollection

internal class AccountMongoRepository(override val collection: CoroutineCollection<Account>) :
    AccountRepository
