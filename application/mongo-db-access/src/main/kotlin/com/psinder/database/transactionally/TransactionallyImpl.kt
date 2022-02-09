package com.psinder.database.transactionally

import com.mongodb.TransactionOptions
import com.mongodb.reactivestreams.client.ClientSession
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.abortTransactionAndAwait
import org.litote.kmongo.coroutine.commitTransactionAndAwait
import java.io.Closeable

class TransactionallyImpl(private val mongoClient: CoroutineClient) : Transactionally {
    override suspend fun <R> call(block: suspend (ClientSession) -> R) = mongoClient.startSession().call(block)
    override suspend fun <R> call(txOptions: TransactionOptions, block: suspend (ClientSession) -> R) =
        mongoClient.startSession().call(txOptions, block)
}

suspend fun <R> ClientSession.call(block: suspend (ClientSession) -> R) {
    this.use { session ->
        session.startTransaction()
        runCatching { block(this) }
            .onSuccess { session.commitTransactionAndAwait() }
            .onFailure { session.abortTransactionAndAwait() }
    }
}

suspend fun <R> ClientSession.call(txOptions: TransactionOptions, block: suspend (ClientSession) -> R) {
    this.use { session ->
        session.startTransaction(txOptions)
        runCatching { block(this) }
            .onSuccess { session.commitTransactionAndAwait() }
            .onFailure { session.abortTransactionAndAwait() }
    }
}