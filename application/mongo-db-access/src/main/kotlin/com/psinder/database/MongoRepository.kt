package com.psinder.database

import arrow.core.Option
import arrow.core.toOption
import com.mongodb.client.result.UpdateResult
import org.bson.conversions.Bson
import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.CoroutineCollection

interface MongoRepository<T : HasId<T>> {
    val collection: CoroutineCollection<T>

    suspend fun findById(id: Id<T>): Option<T> {
        return collection.findOneById(id).toOption()
    }

    suspend fun updateById(id: Id<T>, value: T): UpdateResult {
        return collection.updateOneById(id, value)
    }

    suspend fun save(entity: T): UpdateResult? {
        return collection.save(entity)
    }
}
