package com.psinder.account.activation.subscribers

import arrow.core.nel
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.psinder.account.AccountDto
import com.psinder.account.activation.AccountActivationTokensMongoRepository
import com.psinder.account.activation.AccountActivationTokensRepository
import com.psinder.account.activation.ActivationToken
import com.psinder.account.activation.events.AccountActivationTokenGeneratedEvent
import com.psinder.account.support.DatabaseAndEventStoreTest
import com.psinder.auth.account.AccountId
import com.psinder.events.streamName
import com.psinder.events.toEventData
import com.psinder.shared.jwt.JwtToken
import io.ktor.server.testing.withApplication
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.test.get
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.newId
import strikt.api.expectThat
import strikt.arrow.isSome
import strikt.assertions.first
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo

private val testingModules = module {
    single { AccountActivationTokensProjectionUpdater(get()) }
    single {
        AccountActivationTokensMongoRepository(get<CoroutineDatabase>().getCollection())
    } bind AccountActivationTokensRepository::class
}

class AccountActivationTokensProjectionSubscriberKtTest : DatabaseAndEventStoreTest(testingModules.nel()) {
    private val projectionUpdater = get<AccountActivationTokensProjectionUpdater>()
    private val activationTokensRepository = get<AccountActivationTokensRepository>()

    init {

        describe("AccountActivationTokensProjectionSubscriber") {

            it("should store generated token") {
                withApplication {
                    val application = this.application
                    launch {
                        // arrange
                        val accountId = newId<AccountDto>()
                        val token = JWT.create().sign(Algorithm.HMAC256("secret")).let { JwtToken.createOrThrow(it) }
                        val tokenGeneratedEvent = AccountActivationTokenGeneratedEvent(accountId, token)

                        // act
                        application.accountActivationTokensProjectionSubscriber(eventStoreDb, projectionUpdater)
                        eventStoreDb.appendToStream(
                            tokenGeneratedEvent.streamName,
                            tokenGeneratedEvent.toEventData<AccountActivationTokenGeneratedEvent>()
                        )
                        delay(600)

                        // assert
                        val result = activationTokensRepository.findOneByAccountId(AccountId(accountId.toString()))
                        expectThat(result).isSome().get { value }
                            .and { get { this.accountId }.isEqualTo(AccountId(accountId.toString())) }
                            .and { get { this.tokens }.hasSize(1).first().isEqualTo(ActivationToken(token, false)) }
                    }
                }
            }
        }
    }
}
