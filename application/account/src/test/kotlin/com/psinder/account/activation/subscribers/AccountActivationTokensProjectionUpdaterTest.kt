package com.psinder.account.activation.subscribers

import arrow.core.nel
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.psinder.account.activation.AccountActivationTokensMongoRepository
import com.psinder.account.activation.AccountActivationTokensRepository
import com.psinder.account.activation.events.AccountActivationTokenGeneratedEvent
import com.psinder.auth.account.AccountId
import com.psinder.database.DatabaseTest
import com.psinder.database.recordedEvent
import com.psinder.shared.jwt.JwtToken
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
import strikt.assertions.isFalse

private val testingModules = module {
    single { AccountActivationTokensProjectionUpdater(get()) }
    single {
        AccountActivationTokensMongoRepository(get<CoroutineDatabase>().getCollection())
    } bind AccountActivationTokensRepository::class
}

class AccountActivationTokensProjectionUpdaterTest : DatabaseTest(testingModules.nel()) {
    private val updater = get<AccountActivationTokensProjectionUpdater>()
    private val accountActivationTokensRepository = get<AccountActivationTokensRepository>()

    init {

        describe("AccountActivationTokensProjectionUpdater") {

            it("can update projection") {
                // arrange
                val jwt = JWT.create().sign(Algorithm.HMAC256("secret")).let { JwtToken.createOrThrow(it) }
                val event = AccountActivationTokenGeneratedEvent(newId(), jwt)

                val recordedEvent = event.recordedEvent<AccountActivationTokenGeneratedEvent>()

                // act
                updater.update(recordedEvent)

                // assert
                val accountActivationTokens =
                    accountActivationTokensRepository.findOneByAccountId(AccountId(event.accountId.toString()))
                expectThat(accountActivationTokens)
                    .isSome()
                    .get { value }
                    .and { get { tokens }.hasSize(1) }
                    .and { get { tokens }.first().get { token }.isEqualTo(jwt) }
                    .and { get { tokens }.first().get { used }.isFalse() }
            }
        }
    }
}
