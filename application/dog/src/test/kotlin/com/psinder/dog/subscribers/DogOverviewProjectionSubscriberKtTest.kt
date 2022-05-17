package com.psinder.dog.subscribers

import arrow.core.nel
import com.psinder.auth.principal.authModule
import com.psinder.dog.DogAggregate
import com.psinder.dog.DogOverviewMongoRepository
import com.psinder.dog.DogOverviewRepository
import com.psinder.dog.dogModule
import com.psinder.dog.register
import com.psinder.dog.support.DatabaseAndEventStoreTest
import com.psinder.events.streamName
import com.psinder.events.toEventData
import com.psinder.test.utils.faker
import io.ktor.server.testing.withApplication
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.get
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import strikt.api.expectThat
import strikt.arrow.isSome
import strikt.assertions.isEqualTo

private val testingModules = module {
    single { DogOverviewMongoRepository(get<CoroutineDatabase>().getCollection()) } bind DogOverviewRepository::class
    single { DogProfileProjectionUpdater(get()) }
}

class DogOverviewProjectionSubscriberKtTest : DatabaseAndEventStoreTest(testingModules.nel()) {
    private val dogRepository = get<DogOverviewRepository>()

    init {

        describe("Dog projection subscriber") {

            it("should create account projection on DogRegistered event") {
                withApplication {
                    val application = this.application
                    launch {
                        // arrange
                        val accountContext = faker.authModule.accountContext()
                        val dogRegisteredEvent = DogAggregate.register(
                            accountContext, faker.dogModule.dogName(), faker.dogModule.dogDescription(), emptyList()
                        )

                        // act
                        application.dogOverviewProjectionUpdater(eventStoreDb, get())
                        eventStoreDb.appendToStream(
                            dogRegisteredEvent.streamName,
                            dogRegisteredEvent.toEventData(),
                        )
                        delay(600)

                        // assert
                        val dogProjection = dogRepository.findById(dogRegisteredEvent.dogId.cast())
                        expectThat(dogProjection) {
                            isSome().get { value }.and { get { accountId }.isEqualTo(accountContext.accountId) }
                                .and { get { name }.isEqualTo(dogRegisteredEvent.dogName) }
                                .and { get { description }.isEqualTo(dogRegisteredEvent.dogDescription) }
                                .and { get { images }.isEqualTo(dogRegisteredEvent.images) }
                        }
                    }
                }
            }
        }
    }
}
