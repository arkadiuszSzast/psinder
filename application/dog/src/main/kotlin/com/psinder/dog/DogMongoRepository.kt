package com.psinder.dog

import org.litote.kmongo.coroutine.CoroutineCollection

internal class DogMongoRepository(override val collection: CoroutineCollection<DogProjection>) : DogRepository
