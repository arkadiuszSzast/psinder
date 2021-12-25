package com.psinder.shared.database

import com.psinder.account.AccountMongoRepository
import com.psinder.account.AccountRepository
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase

val repositoriesModule = module {
    single { AccountMongoRepository(get<CoroutineDatabase>().getCollection()) } bind AccountRepository::class
}
