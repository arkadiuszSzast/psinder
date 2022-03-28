package com.psinder.mail

import com.sendgrid.SendGrid
import org.koin.dsl.bind
import org.koin.dsl.module

val sendGridModule = module {
    single { SendGrid(SendGridConfig.apiKey) }
    single { SendGridMailSender(get()) } bind MailSender::class
}
