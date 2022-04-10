package com.psinder.account

import com.psinder.auth.role.Role
import com.psinder.database.HasDatabaseAndTransactionally
import com.psinder.shared.EmailAddress
import com.psinder.shared.date.CreatedDate
import com.psinder.shared.password.HashedPassword
import com.psinder.test.utils.faker
import kotlinx.datetime.TimeZone
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.codifiedEnum

suspend fun HasDatabaseAndTransactionally.createRandomAccount(customize: AccountProjection.() -> Unit = {}): AccountProjection {
    val accountAggregate = faker.randomProvider.randomClassInstance<AccountProjection> {
        typeGenerator { newId<AccountProjection>() }
        typeGenerator { faker.accountModule.emailAddress() }
        typeGenerator { faker.accountModule.name() }
        typeGenerator { faker.accountModule.surname() }
        typeGenerator { faker.accountModule.city() }
        typeGenerator { faker.accountModule.streetName() }
        typeGenerator { faker.accountModule.rawPassword() }
        typeGenerator { faker.accountModule.accountStatus().codifiedEnum() }
        typeGenerator { faker.accountModule.timeZone() }
        typeGenerator { faker.accountModule.lastLoggedInDate() }
        typeGenerator { faker.accountModule.created() }
    }.apply(customize)
    db.getCollection<AccountProjection>().insertOne(accountAggregate)
    return accountAggregate
}

suspend fun HasDatabaseAndTransactionally.createAccount(
    id: Id<AccountProjection> = newId(),
    email: EmailAddress = faker.accountModule.emailAddress(),
    name: Name = faker.accountModule.name(),
    surname: Surname = faker.accountModule.surname(),
    city: City = faker.accountModule.city(),
    streetName: StreetName = faker.accountModule.streetName(),
    password: HashedPassword = faker.accountModule.hashedPassword(),
    created: CreatedDate = faker.accountModule.created(),
    status: CodifiedEnum<AccountStatus, String> = faker.accountModule.accountStatus().codifiedEnum(),
    role: CodifiedEnum<Role, String> = faker.accountModule.role().codifiedEnum(),
    timeZone: TimeZone = faker.accountModule.timeZone(),
    customize: AccountProjection.() -> Unit = {}
): AccountProjection {
    val accountAggregate =
        AccountProjection(
            id,
            email,
            PersonalData(name, surname, AddressData(city, streetName)),
            password,
            created,
            status,
            role,
            timeZone
        ).apply(customize)
    db.getCollection<AccountProjection>().insertOne(accountAggregate)
    return accountAggregate
}
