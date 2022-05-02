package com.psinder.dog

import com.psinder.database.HasId
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import org.litote.kmongo.Id

data class DogAggregate(
    @SerialName("_id") @Contextual override val id: Id<DogAggregate>,
    val name: DogName,
    val images: List<DogProfileImage>
) : HasId<DogAggregate>
