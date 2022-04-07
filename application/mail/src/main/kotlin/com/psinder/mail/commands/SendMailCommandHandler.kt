package com.psinder.mail.commands

import com.psinder.auth.authority.sendingMailsFeature
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.events.appendToStream
import com.psinder.mail.Mail
import com.psinder.mail.MailDto
import com.psinder.mail.MailSender
import com.psinder.mail.MailSendingError
import com.psinder.mail.MailSentResult
import com.psinder.mail.events.MailSendingErrorEvent
import com.psinder.mail.events.MailSentSuccessfullyEvent
import com.psinder.mail.send
import com.psinder.mail.toDomain
import com.trendyol.kediatr.AsyncCommandWithResultHandler
import com.trendyol.kediatr.CommandMetadata
import io.traxter.eventstoredb.EventStoreDB
import mu.KotlinLogging
import org.litote.kmongo.newId

internal class SendMailCommandHandler(
    private val mailSender: MailSender,
    private val eventStore: EventStoreDB,
    private val acl: AuthorizedAccountAbilityProvider
) : AsyncCommandWithResultHandler<SendMailCommand, MailSentResult> {
    private val log = KotlinLogging.logger {}

    override suspend fun handleAsync(command: SendMailCommand): MailSentResult {
        val (mailDto, metadata) = command
        acl.hasAccessTo(sendingMailsFeature).onDeny {
            return handleNoPermissions(mailDto, metadata)
        }

        return when (val event = Mail.send(mailSender, mailDto)) {
            is MailSentSuccessfullyEvent -> {
                log.debug { "Mail with id: ${event.mailId} sent successfully" }
                eventStore.appendToStream(event, metadata)
                MailSentResult.Success(event.mailId.cast())
            }
            is MailSendingErrorEvent -> {
                log.debug { "Error when sending mail with id: ${event.mailId}. Cause: ${event.error}" }
                eventStore.appendToStream(event, metadata)
                MailSentResult.Error(event.mailId.cast(), event.error)
            }
        }
    }

    private suspend fun handleNoPermissions(
        mailDto: MailDto,
        metadata: CommandMetadata?
    ): MailSentResult.Error {
        val mailSendingErrorEvent = mailDto.toMailSendingErrorEvent(
            MailSendingError("Mail send failed because of lack of permissions")
        )

        eventStore.appendToStream(mailSendingErrorEvent, metadata)

        return MailSentResult.Error(mailSendingErrorEvent.mailId.cast(), mailSendingErrorEvent.error)
    }

    private fun MailDto.toMailSendingErrorEvent(reason: MailSendingError) =
        MailSendingErrorEvent(id.cast(), from, to, subject, templateId, variables, reason)
}
