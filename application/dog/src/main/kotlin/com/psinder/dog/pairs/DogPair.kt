package com.psinder.dog.pairs

import com.psinder.dog.DogAggregate
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

@Serializable
data class DogPair(@Contextual val targetDogId: Id<DogAggregate>)
