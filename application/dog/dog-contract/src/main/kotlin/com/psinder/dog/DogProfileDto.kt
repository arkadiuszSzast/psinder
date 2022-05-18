package com.psinder.dog

import com.psinder.auth.account.AccountId
import com.psinder.dog.vote.Vote
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

@Serializable
data class DogProfileDto(@Contextual val id: Id<DogProfileDto>, val accountId: AccountId, val votes: List<Vote>)
