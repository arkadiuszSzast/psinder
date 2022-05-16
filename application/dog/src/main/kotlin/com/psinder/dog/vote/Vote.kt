package com.psinder.dog.vote

import com.psinder.auth.role.Role
import com.psinder.dog.DogAggregate
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import pl.brightinventions.codified.enums.CodifiedEnum

@Serializable
data class Vote(
    @Contextual val targetDogId: Id<DogAggregate>,
    @Serializable(with = VoteOption.CodifiedSerializer::class) val selectedOption: CodifiedEnum<VoteOption, String>
)
