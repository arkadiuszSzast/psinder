package com.psinder.dog

import org.litote.kmongo.coroutine.CoroutineCollection

internal class DogProfileMongoRepository(override val collection: CoroutineCollection<DogProfileProjection>) :
    DogProfileRepository
