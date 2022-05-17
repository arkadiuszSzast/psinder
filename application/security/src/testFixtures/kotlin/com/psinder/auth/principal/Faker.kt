package com.psinder.auth.principal

import com.psinder.auth.account.AccountContext
import com.psinder.auth.account.AccountId
import com.psinder.auth.role.Role
import io.github.serpro69.kfaker.Faker
import org.litote.kmongo.newId
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.codifiedEnum

val Faker.authModule: AuthModule
    get() = AuthModule(this)

class AuthModule(private val faker: Faker) {
    fun accountContext() = object : AccountContext {
        override val accountId: AccountId = AccountId(newId<AccountId>().toString())
        override val role: CodifiedEnum<Role, String> = faker.random.nextEnum<Role>().codifiedEnum()
    }
}
