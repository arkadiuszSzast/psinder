package com.psinder.account.koin

import com.psinder.account.AccountMongoRepository
import com.psinder.account.AccountRepository
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase

val accountRepositoriesModule = module {
    single { AccountMongoRepository(get<CoroutineDatabase>().getCollection()) } bind AccountRepository::class
}
