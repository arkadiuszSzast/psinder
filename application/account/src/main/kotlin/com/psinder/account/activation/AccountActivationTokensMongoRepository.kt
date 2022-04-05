package com.psinder.account.activation

import org.litote.kmongo.coroutine.CoroutineCollection

internal class AccountActivationTokensMongoRepository(override val collection: CoroutineCollection<AccountActivationTokens>) :
    AccountActivationTokensRepository
