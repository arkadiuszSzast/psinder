package com.psinder.dog.subscribers

import arrow.core.nel
import com.psinder.auth.principal.authModule
import com.psinder.dog.DogAggregate
import com.psinder.dog.DogMongoRepository
import com.psinder.dog.DogRepository
import com.psinder.dog.dogModule
import com.psinder.dog.events.DogRegisteredEvent
import com.psinder.dog.register
import com.psinder.dog.support.DatabaseAndEventStoreTest
import com.psinder.events.streamName
import com.psinder.events.toEventData
import com.psinder.test.utils.faker
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.ktor.server.testing.withApplication
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.get
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.brightinventions.codified.enums.codifiedEnum
import strikt.api.expectThat
import strikt.arrow.isSome
import strikt.assertions.isEqualTo
import strikt.assertions.isNull

private val testingModules = module {
    single { DogMongoRepository(get<CoroutineDatabase>().getCollection()) } bind DogRepository::class
    single { DogProjectionUpdater(get()) }
}

class DogProjectionSubscriberKtTest : DatabaseAndEventStoreTest(testingModules.nel()) {
    private val dogRepository = get<DogRepository>()

    init {

        describe("Dog projection subscriber") {

            it("should create account projection on DogRegistered event") {
                withApplication {
                    val application = this.application
                    launch {
                        // arrange
                        val accountContext = faker.authModule.accountContext()
                        val dogRegisteredEvent = DogAggregate.Events.register(
                            accountContext, faker.dogModule.dogName(), faker.dogModule.dogDescription(), emptyList()
                        )

                        // act
                        application.dogProjectionUpdater(eventStoreDb, get())
                        eventStoreDb.appendToStream(
                            dogRegisteredEvent.streamName,
                            dogRegisteredEvent.toEventData<DogRegisteredEvent>(),
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
