package com.psinder.dog

import arrow.optics.optics
import arrow.optics.snoc
import com.psinder.auth.account.AccountId
import com.psinder.auth.account.BelongsToAccount
import com.psinder.auth.account.DogId
import com.psinder.auth.account.DogIdProvider
import com.psinder.database.HasId
import com.psinder.dog.events.DogDislikeSentEvent
import com.psinder.dog.events.DogLikeSentEvent
import com.psinder.dog.events.DogPairFoundEvent
import com.psinder.dog.events.DogRegisteredEvent
import com.psinder.dog.pairs.DogPair
import com.psinder.dog.vote.Vote
import com.psinder.dog.vote.VoteOption
import kotlinx.datetime.Clock
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import pl.brightinventions.codified.enums.codifiedEnum

@optics
@Serializable
data class DogProfileProjection(
    @SerialName("_id") @Contextual override val id: Id<DogProfileProjection>,
    override val accountId: AccountId,
    val name: DogName,
    val description: DogDescription,
    val images: List<DogProfileImage>,
    val votes: List<Vote>,
    val pairs: List<DogPair>
) : HasId<DogProfileProjection>, BelongsToAccount, DogIdProvider {

    override val dogId: DogId
        get() = DogId(id.toString())

    companion object {

        fun applyRegisteredEvent(event: DogRegisteredEvent) = DogProfileProjection(
            event.dogId.cast(),
            event.accountId,
            event.dogName,
            event.dogDescription,
            event.images,
            emptyList(),
            emptyList()
        )

        fun applyDogLikedEvent(source: DogProfileProjection, event: DogLikeSentEvent) =
            DogProfileProjection.votes.modify(source) {
                it.filter { it.targetDogId != event.targetDogId }
                    .snoc(Vote(event.targetDogId, VoteOption.Like.codifiedEnum()))
            }

        fun applyDogDislikedEvent(source: DogProfileProjection, event: DogDislikeSentEvent) =
            DogProfileProjection.votes.modify(source) {
                it.filter { it.targetDogId != event.targetDogId }
                    .snoc(Vote(event.targetDogId, VoteOption.Dislike.codifiedEnum()))
            }

        fun applyDogPairFoundEvent(source: DogProfileProjection, event: DogPairFoundEvent) =
            DogProfileProjection.pairs.modify(source) {
                if (it.none { it.targetDogId == event.pairDogId }) it.snoc(
                    DogPair(
                        event.pairDogId.cast(),
                        Clock.System.now()
                    )
                ) else it
            }
    }
}
