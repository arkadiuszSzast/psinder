package com.psinder.dog

import com.psinder.auth.account.AccountId
import com.psinder.auth.account.BelongsToAccount
import com.psinder.auth.account.DogId
import com.psinder.auth.account.DogIdProvider
import com.psinder.database.HasId
import com.psinder.dog.pairs.DogPair
import com.psinder.dog.vote.Vote
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

@Serializable
data class DogAggregate(
    @SerialName("_id") @Contextual override val id: Id<DogAggregate>,
    override val accountId: AccountId,
    val name: DogName,
    val description: DogDescription,
    val images: List<DogProfileImage>,
    val votes: List<Vote>,
    val pairs: List<DogPair>
) : HasId<DogAggregate>, DogIdProvider, BelongsToAccount {

    override val dogId: DogId
        get() = DogId(id.toString())
}
