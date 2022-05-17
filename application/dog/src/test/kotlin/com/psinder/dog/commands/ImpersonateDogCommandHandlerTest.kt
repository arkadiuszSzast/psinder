package com.psinder.dog.commands

import com.auth0.jwt.algorithms.Algorithm
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.auth.principal.CanDoAnythingAbilityProvider
import com.psinder.auth.principal.authModule
import com.psinder.config.JwtAuthConfig
import com.psinder.database.DatabaseTest
import com.psinder.database.RecordingEventStoreDB
import com.psinder.dog.DogProfileMongoRepository
import com.psinder.dog.DogProfileRepository
import com.psinder.dog.ImpersonatingError
import com.psinder.dog.createDogProfile
import com.psinder.dog.queries.FindDogProfileByIdQueryHandler
import com.psinder.kediatr.kediatrTestModule
import com.psinder.test.utils.faker
import io.traxter.eventstoredb.EventStoreDB
import org.koin.core.component.get
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.newId
import pl.brightinventions.codified.enums.codifiedEnum
import strikt.api.expectThat
import strikt.arrow.isValid
import strikt.assertions.isA
import strikt.assertions.isEqualTo

private val testingModules = module {
    single { CanDoAnythingAbilityProvider() } bind AuthorizedAccountAbilityProvider::class
    single { RecordingEventStoreDB() } bind EventStoreDB::class
    single { ImpersonateDogCommandHandler(JwtAuthConfig, get(), get(), get()) }
    single { FindDogProfileByIdQueryHandler(get(), get()) }
    single { DogProfileMongoRepository(get<CoroutineDatabase>().getCollection()) } bind DogProfileRepository::class
}.plus(kediatrTestModule)

class ImpersonateDogCommandHandlerTest : DatabaseTest(testingModules) {
    private val handler = get<ImpersonateDogCommandHandler>()

    init {

        describe("ImpersonateDogCommandHandler") {

            it("should return impersonator token") {
                // arrange
                val accountContext = faker.authModule.accountContext()
                val dog = createDogProfile(accountId = accountContext.accountId)

                // act
                val result = handler.handleAsync(ImpersonateDogCommand(dog.id.cast(), accountContext))

                // arrange
                expectThat(result)
                    .isA<ImpersonateDogCommandSuccessResult>()
                    .get { token.verify(Algorithm.HMAC256(JwtAuthConfig.secret)) }
                    .isValid()
            }

            it("should return error when trying impersonate dog that belongs to other account") {
                // arrange
                val accountContext = faker.authModule.userAccountContext()
                val otherAccountContext = faker.authModule.userAccountContext()
                val dog = createDogProfile(accountId = otherAccountContext.accountId)

                // act
                val result = handler.handleAsync(ImpersonateDogCommand(dog.id.cast(), accountContext))

                // arrange
                expectThat(result)
                    .isA<ImpersonateDogCommandFailureResult>()
                    .get { error }.isEqualTo(ImpersonatingError.NotAllowed.codifiedEnum())
            }

            it("should return error when trying impersonate not existing dog") {
                // arrange
                val accountContext = faker.authModule.accountContext()
                val dog = createDogProfile(accountId = accountContext.accountId)

                // act
                val result = handler.handleAsync(ImpersonateDogCommand(newId(), accountContext))

                // arrange
                expectThat(result)
                    .isA<ImpersonateDogCommandFailureResult>()
                    .get { error }.isEqualTo(ImpersonatingError.NotFound.codifiedEnum())
            }

            it("should impersonate other user dog when authenticated as admin") {
                // arrange
                val accountContext = faker.authModule.adminAccountContext()
                val otherAccountContext = faker.authModule.userAccountContext()
                val dog = createDogProfile(accountId = otherAccountContext.accountId)

                // act
                val result = handler.handleAsync(ImpersonateDogCommand(dog.id.cast(), accountContext))

                // arrange
                expectThat(result)
                    .isA<ImpersonateDogCommandSuccessResult>()
                    .get { token.verify(Algorithm.HMAC256(JwtAuthConfig.secret)) }
                    .isValid()
            }
        }
    }
}
