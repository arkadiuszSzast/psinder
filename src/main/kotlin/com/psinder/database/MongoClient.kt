package com.psinder.database

import com.psinder.config.DatabaseConfig
import com.mongodb.ConnectionString
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

private val client by lazy { KMongo.createClient(ConnectionString(DatabaseConfig.connectionString)).coroutine }

internal val database by lazy { client.getDatabase(DatabaseConfig.name) }
