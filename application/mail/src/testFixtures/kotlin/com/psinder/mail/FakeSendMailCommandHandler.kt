package com.psinder.mail

import com.psinder.events.streamName
import com.psinder.events.toEventData
import com.psinder.mail.commands.SendMailCommand
import com.psinder.mail.events.MailSendingErrorEvent
import com.psinder.mail.events.MailSentSuccessfullyEvent
import com.trendyol.kediatr.AsyncCommandWithResultHandler
import io.traxter.eventstoredb.EventStoreDB

class FakeSendMailCommandHandler(
    private val mailSender: MailSender,
    private val eventStore: EventStoreDB,
) : AsyncCommandWithResultHandler<SendMailCommand, MailSentResult> {
    override suspend fun handleAsync(command: SendMailCommand): MailSentResult =
        when (val event = command.mail.toDomain().send(mailSender)) {
            is MailSentSuccessfullyEvent -> {
                eventStore.appendToStream(
                    event.streamName,
                    event.toEventData<MailSentSuccessfullyEvent>(command.metadata)
                )
                MailSentResult.Success(event.mailId.cast())
            }
            is MailSendingErrorEvent -> {
                eventStore.appendToStream(event.streamName, event.toEventData<MailSendingErrorEvent>(command.metadata))
                MailSentResult.Error(event.mailId.cast(), event.error)
            }
        }
}
