package com.psinder.mail

import com.psinder.shared.EmailAddress
import io.github.serpro69.kfaker.Faker
import org.litote.kmongo.newId

val Faker.mailModule: MailModule
    get() = MailModule(this)

class MailModule(private val faker: Faker) {
    fun mailSubject() = MailSubject(faker.book.title())
    fun mailTemplateId() = MailTemplateId(faker.random.nextUUID())
    fun mailDto() = MailDto(
        mailSubject(),
        EmailAddress.create("${faker.animal.name()}@mail.com"),
        EmailAddress.create("${faker.familyGuy.character()}@mail.com"),
        mailTemplateId(),
        MailVariables(emptyMap())
    )

    fun mail() = Mail(
        newId(),
        mailSubject(),
        EmailAddress.create("${faker.animal.name()}@mail.com"),
        EmailAddress.create("${faker.familyGuy.character()}@mail.com"),
        mailTemplateId(),
        MailVariables(emptyMap())
    )
}
