package com.psinder.account.subscribers

import arrow.core.nel
import com.psinder.account.activation.commands.GenerateAccountActivationLinkCommand
import com.psinder.account.events.AccountCreatedEvent
import com.psinder.account.config.MailConfig
import com.psinder.auth.authority.generateAccountActivationLinkFeature
import com.psinder.auth.authority.generateAccountActivationTokenFeature
import com.psinder.auth.authority.sendingMailsFeature
import com.psinder.auth.authority.withInjectedAuthorities
import com.psinder.auth.authority.withInjectedAuthoritiesReturning
import com.psinder.events.getAs
import com.psinder.events.getMetadata
import com.psinder.events.toCommandMetadata
import com.psinder.mail.MailDto
import com.psinder.mail.MailProperties
import com.psinder.mail.MailVariables
import com.psinder.mail.commands.SendMailCommand
import com.trendyol.kediatr.CommandBus
import io.ktor.application.Application
import io.ktor.http.Url
import io.traxter.eventstoredb.EventStoreDB
import io.traxter.eventstoredb.StreamGroup
import io.traxter.eventstoredb.StreamName
import kotlinx.coroutines.launch

private val generateActivationLinkAuthorities =
    listOf(generateAccountActivationTokenFeature, generateAccountActivationLinkFeature)

internal fun Application.activationMailSenderSubscriber(
    eventStoreDb: EventStoreDB,
    commandBus: CommandBus,
    mailConfig: MailConfig
) = launch {
    eventStoreDb.subscribeToPersistedStream(
        StreamName(AccountCreatedEvent.fullEventType.streamName()),
        StreamGroup("activation-mail-sender")
    ) { _, event ->
        val accountCreatedEvent = event.event
        val eventMetadata = accountCreatedEvent.getMetadata().orNull()

        accountCreatedEvent.getAs<AccountCreatedEvent>()
            .map {
                val link = withInjectedAuthoritiesReturning(generateActivationLinkAuthorities) {
                    commandBus.executeCommandAsync(
                        GenerateAccountActivationLinkCommand(it.accountId.cast(), eventMetadata?.toCommandMetadata())
                    ).link
                }

                val mailDto = toMailDto(mailConfig.activateAccount, it, link)

                withInjectedAuthorities(sendingMailsFeature.nel()) {
                    commandBus.executeCommandAsync(SendMailCommand(mailDto, eventMetadata?.toCommandMetadata()))
                }
            }
    }
}

private fun toMailDto(mailProperties: MailProperties, accountCreatedEvent: AccountCreatedEvent, link: Url) =
    MailDto(
        mailProperties.subject,
        mailProperties.sender,
        accountCreatedEvent.email,
        mailProperties.templateId,
        MailVariables(
            mapOf(
                "first_name" to accountCreatedEvent.personalData.name.value,
                "activate_account_url" to link.toString()
            )
        )
    )
