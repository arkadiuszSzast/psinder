package com.psinder.account.queries

import arrow.core.nel
import com.psinder.account.AccountMongoRepository
import com.psinder.account.AccountRepository
import com.psinder.account.createAccount
import com.psinder.account.createRandomAccount
import com.psinder.database.DatabaseTest
import com.psinder.shared.EmailAddress
import org.koin.core.component.get
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import strikt.api.expectThat
import strikt.arrow.isNone
import strikt.arrow.isSome
import strikt.assertions.isEqualTo

private val testingModules = module {
    single { FindAccountByEmailQueryHandler(get()) }
    single { AccountMongoRepository(get<CoroutineDatabase>().getCollection()) } bind AccountRepository::class
}

internal class FindAccountByEmailQueryHandlerTest : DatabaseTest(testingModules.nel()) {
    private val handler = get<FindAccountByEmailQueryHandler>()

    init {
        describe("FindAccountByEmailQuery test") {

            it("should be empty when account not exist") {
                // arrange
                val account = createAccount(email = EmailAddress.create("joe@doe.com"))

                // act
                val result = handler.handleAsync(FindAccountByEmailQuery(EmailAddress.create("not_existing@mail.com")))

                // assert
                expectThat(result)
                    .get { accountDto }
                    .isNone()
            }

            it("should found account") {
                // arrange
                val account = createRandomAccount()

                // act
                val result = handler.handleAsync(FindAccountByEmailQuery(account.email))

                // assert
                expectThat(result)
                    .get { accountDto }
                    .isSome()
                    .get { value }
                    .get { id }
                    .isEqualTo(account.id.cast())
            }
        }
    }
}
