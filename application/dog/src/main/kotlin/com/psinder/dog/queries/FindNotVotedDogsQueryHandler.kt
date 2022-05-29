package com.psinder.dog.queries

import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.dog.DogOverviewDto
import com.psinder.dog.DogOverviewProjection
import com.psinder.dog.DogOverviewRepository
import com.psinder.dog.DogProfileRepository
import com.psinder.dog.fromDogOverview
import com.psinder.file.storage.PublicFileUrlResolver
import com.psinder.shared.get
import com.trendyol.kediatr.AsyncQueryHandler
import org.bson.types.ObjectId
import org.litote.kmongo.id.toId

internal class FindNotVotedDogsQueryHandler(
    private val overviewRepository: DogOverviewRepository,
    private val profileRepository: DogProfileRepository,
    private val publicFileUrlResolver: PublicFileUrlResolver,
    private val acl: AuthorizedAccountAbilityProvider
) : AsyncQueryHandler<FindNotVotedDogsQuery, FindNotVotedDogsQueryResponse> {

    override suspend fun handleAsync(query: FindNotVotedDogsQuery): FindNotVotedDogsQueryResponse {
        val (dogContext, range) = query

        val dog = profileRepository.findById(ObjectId(dogContext.dogId.value).toId())
            .filter { acl.canView(it).toBoolean() }
            .get()
        val alreadyVoted = dog.votes.map { it.targetDogId }
        val selfId = dog.id

        val result = overviewRepository.findPageWhereIdNotIn(
            (alreadyVoted.map { it.cast<DogOverviewProjection>() } + selfId.cast()),
            range
        )
            .filter { acl.canView(it).toBoolean() }
            .map { DogOverviewDto.fromDogOverview(it, publicFileUrlResolver) }

        return FindNotVotedDogsQueryResponse(result)
    }
}
