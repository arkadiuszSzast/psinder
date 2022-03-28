package com.psinder.mail.commands

import com.psinder.events.streamName
import com.psinder.events.toEventData
import com.psinder.mail.Mail
import com.psinder.mail.MailSender
import com.psinder.mail.MailSendingErrorEvent
import com.psinder.mail.MailSentResult
import com.psinder.mail.MailSentSuccessfullyEvent
import com.trendyol.kediatr.AsyncCommandWithResultHandler
import io.traxter.eventstoredb.EventStoreDB
import mu.KotlinLogging

internal class SendMailCommandHandler(private val mailSender: MailSender, private val eventStore: EventStoreDB) :
    AsyncCommandWithResultHandler<SendMailCommand, MailSentResult> {
    private val log = KotlinLogging.logger {}

    override suspend fun handleAsync(command: SendMailCommand): MailSentResult {
        val mail = command.mail
        val result = mailSender.send(mail)

        when (result) {
            is MailSentResult.Success -> MailSentSuccessfullyEvent.create(mail).also {
                log.debug { "Mail with id: ${it.mailId} sent successfully" }
                eventStore.appendToStream(it.streamName, it.toEventData<Mail, MailSentSuccessfullyEvent>())
            }
            is MailSentResult.Error -> MailSendingErrorEvent.create(mail, result.cause).also {
                log.debug { "Error when sending mail with id: ${it.mailId}. Cause: ${it.error}" }
                eventStore.appendToStream(it.streamName, it.toEventData<Mail, MailSendingErrorEvent>())
            }
        }

        return result
    }
}
