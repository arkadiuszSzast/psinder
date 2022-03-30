package com.psinder.mail.commands

import com.psinder.auth.authority.sendingMailsFeature
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.events.streamName
import com.psinder.events.toEventData
import com.psinder.mail.Mail
import com.psinder.mail.MailSender
import com.psinder.mail.MailSendingErrorEvent
import com.psinder.mail.MailSentResult
import com.psinder.mail.MailSentSuccessfullyEvent
import com.psinder.mail.toDomain
import com.trendyol.kediatr.AsyncCommandWithResultHandler
import io.traxter.eventstoredb.EventStoreDB
import mu.KotlinLogging

internal class SendMailCommandHandler(
    private val mailSender: MailSender,
    private val eventStore: EventStoreDB,
    private val acl: AuthorizedAccountAbilityProvider
) : AsyncCommandWithResultHandler<SendMailCommand, MailSentResult> {
    private val log = KotlinLogging.logger {}

    override suspend fun handleAsync(command: SendMailCommand): MailSentResult {
        acl.ensure().hasAccessTo(sendingMailsFeature) // TODO handle it
        val (mailDto, metadata) = command

        return when (val event = mailDto.toDomain().send(mailSender)) {
            is MailSentSuccessfullyEvent -> {
                log.debug { "Mail with id: ${event.mailId} sent successfully" }
                eventStore.appendToStream(
                    event.streamName,
                    event.toEventData<Mail, MailSentSuccessfullyEvent>(metadata)
                )
                MailSentResult.Success(event.mailId.cast())
            }
            is MailSendingErrorEvent -> {
                log.debug { "Error when sending mail with id: ${event.mailId}. Cause: ${event.error}" }
                eventStore.appendToStream(event.streamName, event.toEventData<Mail, MailSendingErrorEvent>(metadata))
                MailSentResult.Error(event.mailId.cast(), event.error)
            }
        }
    }
}
