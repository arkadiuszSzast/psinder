package com.psinder.dog

import com.psinder.database.MongoRepository
import com.psinder.shared.EmailAddress
import org.litote.kmongo.eq

internal interface DogRepository : MongoRepository<DogProjection>
