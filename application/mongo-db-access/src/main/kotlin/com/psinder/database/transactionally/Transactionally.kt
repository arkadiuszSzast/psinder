package com.psinder.database.transactionally

import com.mongodb.ClientSessionOptions
import com.mongodb.TransactionOptions
import com.mongodb.reactivestreams.client.ClientSession
import org.litote.kmongo.coroutine.CoroutineClient

interface Transactionally {
    suspend fun <R> call(block: suspend (ClientSession) -> R)
    suspend fun <R> call(txOptions: TransactionOptions, block: suspend (ClientSession) -> R)
}