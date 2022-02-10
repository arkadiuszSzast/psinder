package com.psinder.database

import com.mongodb.ConnectionString
import com.psinder.database.transactionally.Transactionally
import com.psinder.database.transactionally.TransactionallyImpl
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val kmongoTestingModule = module {
    single { KMongo.createClient(ConnectionString("mongodb://${MongoContainer.host}:${MongoContainer.port}")).coroutine }
    factory { get<CoroutineClient>().getDatabase("test") }
    single { TransactionallyImpl(get()) } bind Transactionally::class
}
