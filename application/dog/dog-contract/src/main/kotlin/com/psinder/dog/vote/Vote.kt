package com.psinder.dog.vote

import com.psinder.dog.DogProfileDto
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.codifiedEnum

@Serializable
data class Vote(
    @Contextual val targetDogId: Id<DogProfileDto>,
    @Serializable(with = VoteOption.CodifiedSerializer::class) val selectedOption: CodifiedEnum<VoteOption, String>
)

fun Collection<Vote>.likes() = filter { it.selectedOption == VoteOption.Like.codifiedEnum() }