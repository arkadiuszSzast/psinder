package com.psinder.dog

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

@Serializable
data class DogDto(@Contextual val id: Id<DogDto>)
