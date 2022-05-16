package com.psinder.mail

import org.koin.dsl.bind
import org.koin.dsl.module

val recordingMailSenderModule = module {
    single { RecordingMailSender() } bind MailSender::class
    single { FakeSendMailCommandHandler(get(), get()) }
}
