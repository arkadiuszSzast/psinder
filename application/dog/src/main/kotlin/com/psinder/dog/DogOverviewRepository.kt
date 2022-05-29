package com.psinder.dog

import com.psinder.database.MongoRepository
import io.ktor.http.ContentRange
import org.litote.kmongo.Id
import org.litote.kmongo.nin

internal interface DogOverviewRepository : MongoRepository<DogOverviewProjection> {
    suspend fun findPageWhereIdNotIn(
        ids: List<Id<DogOverviewProjection>>,
        range: ContentRange.Bounded
    ): List<DogOverviewProjection> {
        return collection.find(DogOverviewProjection::id nin ids)
            .limit((range.to - range.from).toInt())
            .skip(range.from.toInt())
            .partial(true)
            .descendingSort(DogOverviewProjection::id).toList()
    }
}
