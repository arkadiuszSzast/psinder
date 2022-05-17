package com.psinder.dog

import com.psinder.auth.account.AccountId
import com.psinder.database.HasDatabaseAndTransactionally
import com.psinder.dog.pairs.DogPair
import com.psinder.dog.vote.Vote
import com.psinder.test.utils.faker
import org.litote.kmongo.Id
import org.litote.kmongo.newId

suspend fun HasDatabaseAndTransactionally.createRandomDogProfile(customize: DogProfileProjection.() -> Unit = {}): DogProfileProjection {
    val dogProfile = faker.randomProvider.randomClassInstance<DogProfileProjection> {
        typeGenerator { newId<DogProfileProjection>() }
        typeGenerator { AccountId(newId<AccountId>().toString()) }
        typeGenerator { faker.dogModule.dogName() }
        typeGenerator { faker.dogModule.dogDescription() }
        typeGenerator { emptyList<DogProfileImage>() }
        typeGenerator { emptyList<Vote>() }
        typeGenerator { emptyList<DogPair>() }
    }.apply(customize)
    db.getCollection<DogProfileProjection>().insertOne(dogProfile)
    return dogProfile
}

suspend fun HasDatabaseAndTransactionally.createDogProfile(
    id: Id<DogProfileProjection> = newId(),
    accountId: AccountId = AccountId(newId<AccountId>().toString()),
    dogName: DogName = faker.dogModule.dogName(),
    dogDescription: DogDescription = faker.dogModule.dogDescription(),
    images: List<DogProfileImage> = emptyList(),
    votes: List<Vote> = emptyList(),
    pairs: List<DogPair> = emptyList(),
    customize: DogProfileProjection.() -> Unit = {}
): DogProfileProjection {
    val dogProfile = DogProfileProjection(id, accountId, dogName, dogDescription, images, votes, pairs).apply(customize)
    db.getCollection<DogProfileProjection>().insertOne(dogProfile)
    return dogProfile
}
