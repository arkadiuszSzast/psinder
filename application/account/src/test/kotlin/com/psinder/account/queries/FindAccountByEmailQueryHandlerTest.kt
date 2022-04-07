package com.psinder.account.queries

import arrow.core.nel
import com.psinder.account.AccountDto
import com.psinder.account.AccountMongoRepository
import com.psinder.account.AccountRepository
import com.psinder.account.createAccount
import com.psinder.account.createRandomAccount
import com.psinder.account.fromAccount
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.auth.principal.CanDoAnythingAbilityProvider
import com.psinder.auth.principal.DenyAllAbilityProvider
import com.psinder.database.DatabaseTest
import com.psinder.shared.EmailAddress
import org.koin.core.component.get
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import strikt.api.expectThat
import strikt.arrow.isNone
import strikt.arrow.isSome
import strikt.assertions.isEqualTo

private val testingModules = module {
    single(named("allowAll")) { FindAccountByEmailQueryHandler(get(), get(named("allowAll"))) }
    single(named("denyAll")) { FindAccountByEmailQueryHandler(get(), get(named("denyAll"))) }
    single(named("allowAll")) { CanDoAnythingAbilityProvider() } bind AuthorizedAccountAbilityProvider::class
    single(named("denyAll")) { DenyAllAbilityProvider() } bind AuthorizedAccountAbilityProvider::class
    single { AccountMongoRepository(get<CoroutineDatabase>().getCollection()) } bind AccountRepository::class
}

internal class FindAccountByEmailQueryHandlerTest : DatabaseTest(testingModules.nel()) {
    private val allowingHandler = get<FindAccountByEmailQueryHandler>(named("allowAll"))
    private val denyingHandler = get<FindAccountByEmailQueryHandler>(named("denyAll"))

    init {
        describe("FindAccountByEmailQuery test") {

            it("returns none when no permission to view") {
                // arrange
                val account = createRandomAccount()

                // act
                val result = denyingHandler.handleAsync(FindAccountByEmailQuery(account.email))

                expectThat(result.account)
                    .isNone()
            }

            it("should be empty when account not exist") {
                // arrange
                val account = createAccount(email = EmailAddress.create("joe@doe.com"))

                // act
                val result =
                    allowingHandler.handleAsync(FindAccountByEmailQuery(EmailAddress.create("not_existing@mail.com")))

                // assert
                expectThat(result)
                    .get { this.account }
                    .isNone()
            }

            it("should found account") {
                // arrange
                val account = createRandomAccount()

                // act
                val result = allowingHandler.handleAsync(FindAccountByEmailQuery(account.email))

                // assert
                expectThat(result)
                    .get { this.account }
                    .isSome()
                    .get { value }
                    .isEqualTo(AccountDto.fromAccount(account))
            }
        }
    }
}
