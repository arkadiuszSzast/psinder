package com.psinder.dog.queries

import arrow.core.nel
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.auth.principal.CanDoAnythingAbilityProvider
import com.psinder.auth.principal.DenyAllAbilityProvider
import com.psinder.database.DatabaseTest
import com.psinder.dog.DogProfileDto
import com.psinder.dog.DogProfileMongoRepository
import com.psinder.dog.DogProfileRepository
import com.psinder.dog.createRandomDogProfile
import com.psinder.dog.fromDogProfile
import org.koin.core.component.get
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.newId
import strikt.api.expectThat
import strikt.arrow.isNone
import strikt.arrow.isSome
import strikt.assertions.isEqualTo

private val testingModules = module {
    single(named("allowAll")) { FindDogProfileByIdQueryHandler(get(), get(named("allowAll"))) }
    single(named("denyAll")) { FindDogProfileByIdQueryHandler(get(), get(named("denyAll"))) }
    single(named("allowAll")) { CanDoAnythingAbilityProvider() } bind AuthorizedAccountAbilityProvider::class
    single(named("denyAll")) { DenyAllAbilityProvider() } bind AuthorizedAccountAbilityProvider::class
    single { DogProfileMongoRepository(get<CoroutineDatabase>().getCollection()) } bind DogProfileRepository::class
}

class FindDogProfileByIdQueryHandlerTest : DatabaseTest(testingModules.nel()) {
    private val allowingHandler = get<FindDogProfileByIdQueryHandler>(named("allowAll"))
    private val denyingHandler = get<FindDogProfileByIdQueryHandler>(named("denyAll"))

    init {

        describe("FindDogProfileByIdQueryHandler") {

            it("returns none when no permission to view") {
                // arrange
                val dog = createRandomDogProfile()

                // act
                val result = denyingHandler.handleAsync(FindDogProfileByIdQuery(dog.id.cast()))

                expectThat(result.dog)
                    .isNone()
            }
        }

        it("returns none when account not found") {
            // arrange
            val dog = createRandomDogProfile()

            // act
            val result = allowingHandler.handleAsync(FindDogProfileByIdQuery(newId()))

            expectThat(result.dog)
                .isNone()
        }

        it("returns account") {
            // arrange
            val dog = createRandomDogProfile()

            // act
            val result = allowingHandler.handleAsync(FindDogProfileByIdQuery(dog.id.cast()))

            expectThat(result.dog)
                .isSome()
                .get { value }
                .isEqualTo(DogProfileDto.fromDogProfile(dog))
        }
    }
}
