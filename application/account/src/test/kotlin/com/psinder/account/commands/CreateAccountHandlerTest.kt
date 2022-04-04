package com.psinder.account.commands

import com.psinder.account.AccountMongoRepository
import com.psinder.account.AccountRepository
import com.psinder.account.queries.FindAccountByEmailQueryHandler
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.auth.principal.CanDoAnythingAbilityProvider
import com.psinder.database.DatabaseTest
import com.psinder.kediatr.kediatrTestModule
import org.koin.core.component.get
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase

private val testingModules = module {
    single { CanDoAnythingAbilityProvider() } bind AuthorizedAccountAbilityProvider::class
    single { FindAccountByEmailQueryHandler(get()) }
    single { AccountMongoRepository(get<CoroutineDatabase>().getCollection()) } bind AccountRepository::class
    single { CreateAccountHandler(get(), get(), get()) }
}.plus(kediatrTestModule)

class CreateAccountHandlerTest : DatabaseTest(*testingModules.toTypedArray()) {
    private val handler = get<FindAccountByEmailQueryHandler>()

    init {
        describe("CreateAccountHandle") {

            it("should create account") {
            }
        }
    }
}
