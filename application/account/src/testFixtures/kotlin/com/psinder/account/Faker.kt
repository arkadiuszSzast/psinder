package com.psinder.account

import com.psinder.auth.role.Role
import com.psinder.shared.EmailAddress
import com.psinder.shared.password.RawPassword
import com.psinder.test.utils.date
import io.github.serpro69.kfaker.Faker
import kotlinx.datetime.TimeZone
import java.util.concurrent.TimeUnit

val Faker.accountModule: AccountModule
    get() = AccountModule(this)

class AccountModule(private val faker: Faker) {
    fun emailAddress() = EmailAddress.create("${faker.pokemon.names()}@mail.com")
    fun name() = Name.create(faker.name.firstName())
    fun surname() = Surname.create(faker.name.lastName())
    fun personalData() = PersonalData(name(), surname(), addressData())
    fun city() = City.create(faker.address.city())
    fun streetName() = StreetName.create(faker.address.streetName())
    fun addressData() = AddressData.create(faker.address.city(), faker.address.streetName())
    fun rawPassword() = RawPassword(faker.friends.characters())
    fun hashedPassword() = rawPassword().hashpw()
    fun accountStatus() = faker.random.nextEnum<AccountStatus>()
    fun role() = faker.random.nextEnum<Role>()
    fun timeZone() = TimeZone.of(faker.address.timeZone())
    fun lastLoggedInDate(atMost: Long = 10, unit: TimeUnit = TimeUnit.DAYS) =
        LastLoggedInDate(faker.date.localDateTime.past(atMost, unit))
}
