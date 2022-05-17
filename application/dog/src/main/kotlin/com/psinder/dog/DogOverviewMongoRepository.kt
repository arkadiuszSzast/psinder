package com.psinder.dog

import org.litote.kmongo.coroutine.CoroutineCollection

internal class DogOverviewMongoRepository(override val collection: CoroutineCollection<DogOverviewProjection>) :
    DogOverviewRepository
