package com.psinder.dog.koin

import com.psinder.dog.DogOverviewMongoRepository
import com.psinder.dog.DogOverviewRepository
import com.psinder.dog.DogProfileMongoRepository
import com.psinder.dog.DogProfileRepository
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase

val dogRepositoriesModule = module {
    single { DogProfileMongoRepository(get<CoroutineDatabase>().getCollection()) } bind DogProfileRepository::class
    single { DogOverviewMongoRepository(get<CoroutineDatabase>().getCollection()) } bind DogOverviewRepository::class
}
