package com.psinder.account

import com.psinder.account.activation.AccountActivationTokensMongoRepository
import com.psinder.account.activation.AccountActivationTokensRepository
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase

val accountRepositoriesModule = module {
    single { AccountMongoRepository(get<CoroutineDatabase>().getCollection()) } bind AccountRepository::class
    single {
        AccountActivationTokensMongoRepository(get<CoroutineDatabase>().getCollection())
    } bind AccountActivationTokensRepository::class
}
