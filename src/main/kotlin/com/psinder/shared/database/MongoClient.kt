package com.psinder.shared.database

import com.mongodb.ConnectionString
import com.psinder.config.DatabaseConfig
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

internal val kmongoModule = module {
    single { KMongo.createClient(ConnectionString(DatabaseConfig.connectionString)).coroutine }
    factory { get<CoroutineClient>().getDatabase(DatabaseConfig.name) }
}
