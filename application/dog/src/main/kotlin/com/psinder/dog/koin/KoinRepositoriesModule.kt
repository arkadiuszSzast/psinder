package com.psinder.dog.koin

import com.psinder.dog.DogMongoRepository
import com.psinder.dog.DogRepository
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase

val dogRepositoriesModule = module {
    single { DogMongoRepository(get<CoroutineDatabase>().getCollection()) } bind DogRepository::class
}
