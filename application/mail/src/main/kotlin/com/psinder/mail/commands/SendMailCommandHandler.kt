package com.psinder.mail.commands

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

internal class SendMailCommandHandler(private val mailSender: MailSender, private val eventStore: EventStoreDB) :
    AsyncCommandWithResultHandler<SendMailCommand, MailSentResult> {
    private val log = KotlinLogging.logger {}

    override suspend fun handleAsync(command: SendMailCommand): MailSentResult {
        val mail = command.mail.toDomain()

        return when (val event = mail.send(mailSender)) {
            is MailSentSuccessfullyEvent -> {
                log.debug { "Mail with id: ${event.mailId} sent successfully" }
                eventStore.appendToStream(event.streamName, event.toEventData<Mail, MailSentSuccessfullyEvent>())
                MailSentResult.Success(event.mailId.cast())
            }
            is MailSendingErrorEvent -> {
                log.debug { "Error when sending mail with id: ${event.mailId}. Cause: ${event.error}" }
                eventStore.appendToStream(event.streamName, event.toEventData<Mail, MailSendingErrorEvent>())
                MailSentResult.Error(event.mailId.cast(), event.error)
            }
        }
    }
}
