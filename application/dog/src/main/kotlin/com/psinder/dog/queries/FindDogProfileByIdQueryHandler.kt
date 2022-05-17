package com.psinder.dog.queries

import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.dog.DogProfileDto
import com.psinder.dog.DogProfileRepository
import com.psinder.dog.fromDogProfile
import com.trendyol.kediatr.AsyncQueryHandler
import mu.KotlinLogging

internal class FindDogProfileByIdQueryHandler(
    private val dogRepository: DogProfileRepository,
    private val acl: AuthorizedAccountAbilityProvider
) : AsyncQueryHandler<FindDogProfileByIdQuery, FindDogProfileByIdQueryResult> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(query: FindDogProfileByIdQuery): FindDogProfileByIdQueryResult {
        val id = query.dogId

        return dogRepository.findById(id.cast())
            .tap { logger.debug { "Found dog with id: [$id]" } }
            .tapNone { logger.debug { "Dog with id: [$id] not found" } }
            .filter { acl.canView(it).toBoolean() }
            .map { DogProfileDto.fromDogProfile(it) }
            .let { FindDogProfileByIdQueryResult(it) }
    }
}
