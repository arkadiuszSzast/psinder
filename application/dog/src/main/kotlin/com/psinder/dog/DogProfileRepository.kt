package com.psinder.dog

import com.psinder.database.MongoRepository

internal interface DogProfileRepository : MongoRepository<DogProfileProjection>
