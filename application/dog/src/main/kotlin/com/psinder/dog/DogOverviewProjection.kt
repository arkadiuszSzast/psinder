package com.psinder.dog

import com.psinder.auth.account.AccountId
import com.psinder.auth.account.BelongsToAccount
import com.psinder.auth.account.DogId
import com.psinder.auth.account.DogIdProvider
import com.psinder.database.HasId
import com.psinder.dog.events.DogRegisteredEvent
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

@Serializable
data class DogOverviewProjection(
    @SerialName("_id") @Contextual override val id: Id<DogOverviewProjection>,
    override val accountId: AccountId,
    val name: DogName,
    val description: DogDescription,
    val images: List<DogProfileImage>,
) : HasId<DogOverviewProjection>, BelongsToAccount, DogIdProvider {

    override val dogId: DogId
        get() = DogId(id.toString())

    companion object {

        fun applyRegisteredEvent(event: DogRegisteredEvent) = DogOverviewProjection(
            event.dogId.cast(),
            event.accountId,
            event.dogName,
            event.dogDescription,
            event.images,
        )
    }
}
