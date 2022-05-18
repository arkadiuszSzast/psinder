package com.psinder.dog.requests

import com.psinder.dog.DogProfileDto
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

@Serializable
data class LikeDogRequest(@Contextual val targetDogId: Id<DogProfileDto>)
