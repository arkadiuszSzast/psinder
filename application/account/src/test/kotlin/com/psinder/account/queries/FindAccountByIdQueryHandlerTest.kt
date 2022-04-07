package com.psinder.account.queries

import arrow.core.nel
import com.psinder.account.AccountDto
import com.psinder.account.AccountMongoRepository
import com.psinder.account.AccountRepository
import com.psinder.account.createRandomAccount
import com.psinder.account.fromAccount
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.auth.principal.CanDoAnythingAbilityProvider
import com.psinder.auth.principal.DenyAllAbilityProvider
import com.psinder.database.DatabaseTest
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
    single(named("allowAll")) { FindAccountByIdQueryHandler(get(), get(named("allowAll"))) }
    single(named("denyAll")) { FindAccountByIdQueryHandler(get(), get(named("denyAll"))) }
    single(named("allowAll")) { CanDoAnythingAbilityProvider() } bind AuthorizedAccountAbilityProvider::class
    single(named("denyAll")) { DenyAllAbilityProvider() } bind AuthorizedAccountAbilityProvider::class
    single { AccountMongoRepository(get<CoroutineDatabase>().getCollection()) } bind AccountRepository::class
}

class FindAccountByIdQueryHandlerTest : DatabaseTest(testingModules.nel()) {
    private val allowingHandler = get<FindAccountByIdQueryHandler>(named("allowAll"))
    private val denyingHandler = get<FindAccountByIdQueryHandler>(named("denyAll"))

    init {

        describe("FindAccountByIdQueryHandler") {

            it("returns none when no permission to view") {
                // arrange
                val account = createRandomAccount()

                // act
                val result = denyingHandler.handleAsync(FindAccountByIdQuery(account.id.cast()))

                expectThat(result.account)
                    .isNone()
            }
        }

        it("returns none when account not found") {
            // arrange
            val account = createRandomAccount()

            // act
            val result = allowingHandler.handleAsync(FindAccountByIdQuery(newId()))

            expectThat(result.account)
                .isNone()
        }

        it("returns account") {
            // arrange
            val account = createRandomAccount()

            // act
            val result = allowingHandler.handleAsync(FindAccountByIdQuery(account.id.cast()))

            expectThat(result.account)
                .isSome()
                .get { value }
                .isEqualTo(AccountDto.fromAccount(account))
        }
    }
}
