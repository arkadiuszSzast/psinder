package com.psinder.account

import com.psinder.database.HasDatabaseAndTransactionally
import com.psinder.shared.EmailAddress
import com.psinder.shared.password.HashedPassword
import com.psinder.test.utils.faker
import kotlinx.datetime.TimeZone
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.codifiedEnum

suspend fun HasDatabaseAndTransactionally.createRandomAccount(customize: Account.() -> Unit = {}): Account {
    val account = faker.randomProvider.randomClassInstance<Account> {
        typeGenerator { newId<Account>() }
        typeGenerator { faker.accountModule.emailAddress() }
        typeGenerator { faker.accountModule.name() }
        typeGenerator { faker.accountModule.surname() }
        typeGenerator { faker.accountModule.city() }
        typeGenerator { faker.accountModule.streetName() }
        typeGenerator { faker.accountModule.rawPassword() }
        typeGenerator { faker.accountModule.accountStatus().codifiedEnum() }
        typeGenerator { faker.accountModule.timeZone() }
        typeGenerator { faker.accountModule.lastLoggedInDate() }
    }.apply(customize)
    db.getCollection<Account>().insertOne(account)
    return account
}

suspend fun HasDatabaseAndTransactionally.createAccount(
    id: Id<Account> = newId(),
    email: EmailAddress = faker.accountModule.emailAddress(),
    name: Name = faker.accountModule.name(),
    surname: Surname = faker.accountModule.surname(),
    city: City = faker.accountModule.city(),
    streetName: StreetName = faker.accountModule.streetName(),
    password: HashedPassword = faker.accountModule.hashedPassword(),
    status: CodifiedEnum<AccountStatus, String> = faker.accountModule.accountStatus().codifiedEnum(),
    timeZone: TimeZone = faker.accountModule.timeZone(),
    customize: Account.() -> Unit = {}
): Account {
    val account =
        Account(id, email, PersonalData(name, surname, AddressData(city, streetName)), password, status, timeZone)
            .apply(customize)
    db.getCollection<Account>().insertOne(account)
    return account
}
