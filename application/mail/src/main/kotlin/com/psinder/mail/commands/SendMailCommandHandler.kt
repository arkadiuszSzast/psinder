package com.psinder.mail.commands

import com.psinder.auth.authority.sendingMailsFeature
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.events.streamName
import com.psinder.events.toEventData
import com.psinder.mail.MailDto
import com.psinder.mail.MailSender
import com.psinder.mail.MailSendingError
import com.psinder.mail.events.MailSendingErrorEvent
import com.psinder.mail.MailSentResult
import com.psinder.mail.events.MailSentSuccessfullyEvent
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
        val (mailDto, metadata) = command
        acl.hasAccessTo(sendingMailsFeature).onDeny {
            val mailSendingErrorEvent = mailDto.toMailSendingErrorEvent(
                MailSendingError("Mail send failed because of lack of permissions")
            )

            eventStore.appendToStream(
                mailSendingErrorEvent.streamName,
                mailSendingErrorEvent.toEventData<MailSendingErrorEvent>(metadata)
            )

            return MailSentResult.Error(mailSendingErrorEvent.mailId.cast(), mailSendingErrorEvent.error)
        }

        return when (val event = mailDto.toDomain().send(mailSender)) {
            is MailSentSuccessfullyEvent -> {
                log.debug { "Mail with id: ${event.mailId} sent successfully" }
                eventStore.appendToStream(
                    event.streamName,
                    event.toEventData<MailSentSuccessfullyEvent>(metadata)
                )
                MailSentResult.Success(event.mailId.cast())
            }
            is MailSendingErrorEvent -> {
                log.debug { "Error when sending mail with id: ${event.mailId}. Cause: ${event.error}" }
                eventStore.appendToStream(event.streamName, event.toEventData<MailSendingErrorEvent>(metadata))
                MailSentResult.Error(event.mailId.cast(), event.error)
            }
        }
    }

    private fun MailDto.toMailSendingErrorEvent(reason: MailSendingError) = MailSendingErrorEvent(
        id.cast(),
        from,
        to,
        subject,
        templateId,
        variables,
        reason
    )
}
