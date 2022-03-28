package com.psinder.mail

import com.psinder.shared.EmailAddress
import io.github.serpro69.kfaker.Faker

val Faker.mailModule: MailModule
    get() = MailModule(this)

class MailModule(private val faker: Faker) {
    fun mailSubject() = MailSubject(faker.book.title())
    fun mailTemplateId() = MailTemplateId(faker.random.nextUUID())
    fun mail() = Mail(
        mailSubject(),
        EmailAddress.create("${faker.animal.name()}@mail.com"),
        EmailAddress.create("${faker.familyGuy.character()}@mail.com"),
        mailTemplateId(),
        MailVariables(emptyMap())
    )
}
