package com.psinder.dog.commands

import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.auth.principal.CanDoAnythingAbilityProvider
import com.psinder.auth.principal.authModule
import com.psinder.database.RecordingEventStoreDB
import com.psinder.dog.dogAggregateType
import com.psinder.dog.dogModule
import com.psinder.dog.events.DogRegisteredEvent
import com.psinder.events.getAs
import com.psinder.kediatr.kediatrTestModule
import com.psinder.test.utils.faker
import com.psinder.test.utils.withKoin
import com.trendyol.kediatr.CommandBus
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.traxter.eventstoredb.EventStoreDB
import io.traxter.eventstoredb.StreamName
import org.koin.dsl.bind
import org.koin.dsl.module
import strikt.api.expectThat
import strikt.arrow.isRight
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo

private val testingModules = module {
    single { CanDoAnythingAbilityProvider() } bind AuthorizedAccountAbilityProvider::class
    single { RecordingEventStoreDB() } bind EventStoreDB::class
    single { RegisterDogCommandHandler(get(), get(), get()) }
}.plus(kediatrTestModule)

class RegisterDogCommandHandlerTest : DescribeSpec() {

    init {

        describe("RegisterDogCommandHandler") {

            it("should register a dog") {
                withKoin(testingModules) {
                    // arrange
                    val commandBus = get<CommandBus>()
                    val eventStoreDb = get<RecordingEventStoreDB>()

                    val request = faker.dogModule.registerDogRequest()
                    val accountContext = faker.authModule.accountContext()

                    // act
                    val result = commandBus.executeCommandAsync(RegisterDogCommand(request, accountContext))

                    // assert
                    expectThat(result.dogId).shouldNotBeNull()
                    val events =
                        eventStoreDb.readStream(StreamName("${dogAggregateType.type}-${result.dogId}")).events
                    expectThat(events) {
                        hasSize(1)
                        get { first().originalEvent.getAs<DogRegisteredEvent>() }
                            .isRight()
                            .get { value }
                            .and { get { accountId }.isEqualTo(accountContext.accountId) }
                            .and { get { dogName }.isEqualTo(request.dogName) }
                            .and { get { dogDescription }.isEqualTo(request.dogDescription) }
                    }
                }
            }
        }
    }
}
