package com.psinder.database

import com.mongodb.ConnectionString
import com.psinder.database.config.DatabaseConfig
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val kmongoModule = module {
    single { KMongo.createClient(ConnectionString(DatabaseConfig.connectionString)).coroutine }
    factory { get<CoroutineClient>().getDatabase(DatabaseConfig.name) }
}
