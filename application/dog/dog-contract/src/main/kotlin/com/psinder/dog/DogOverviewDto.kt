package com.psinder.dog

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

@Serializable
data class DogOverviewDto(
    @Contextual val id: Id<DogOverviewDto>,
    val name: DogName,
    val description: DogDescription,
    val images: List<String>
)
