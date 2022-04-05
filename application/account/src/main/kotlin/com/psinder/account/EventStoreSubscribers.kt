package com.psinder.account

import com.psinder.account.activation.subscribers.accountActivationTokensProjectionSubscriber
import com.psinder.account.config.MailConfig
import com.psinder.account.subscribers.accountProjectionUpdater
import com.psinder.account.subscribers.activationMailSenderSubscriber
import io.ktor.application.Application
import org.koin.ktor.ext.get

internal fun Application.configureEventStoreSubscribers() {
    accountProjectionUpdater(get(), get())
    activationMailSenderSubscriber(get(), get(), MailConfig)
    accountActivationTokensProjectionSubscriber(get(), get())
}
