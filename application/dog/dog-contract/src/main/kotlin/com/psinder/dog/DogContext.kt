package com.psinder.dog

import com.psinder.auth.account.AccountId
import com.psinder.auth.account.AccountIdProvider
import com.psinder.auth.account.DogId
import com.psinder.auth.account.DogIdProvider

interface DogContext : DogIdProvider, AccountIdProvider {
    override val dogId: DogId
    override val accountId: AccountId
}
