package com.psinder.mail.commands

import com.psinder.database.RecordingEventStoreDB
import com.psinder.mail.MailSender
import com.psinder.mail.MailSendingError
import com.psinder.mail.MailSendingErrorEvent
import com.psinder.mail.MailSentResult
import com.psinder.mail.MailSentSuccessfullyEvent
import com.psinder.mail.RecordingMailSender
import com.psinder.mail.mailAggregateType
import com.psinder.mail.mailModule
import com.psinder.mail.toDomain
import com.psinder.shared.EmailAddress
import com.psinder.test.utils.faker
import com.psinder.test.utils.withKoin
import io.kotest.core.spec.style.DescribeSpec
import io.traxter.eventstoredb.EventStoreDB
import io.traxter.eventstoredb.StreamName
import org.koin.dsl.bind
import org.koin.dsl.module
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

class SendMailCommandHandlerTest : DescribeSpec() {

    init {

        describe("SendMailCommandHandler") {
            val modules = module {
                single {
                    RecordingMailSender {
                        when (it.to) {
                            EmailAddress.create("invalid@mail.com") ->
                                MailSentResult.Error(it.id.cast(), MailSendingError("Invalid mail address"))
                            else -> MailSentResult.Success(it.id.cast())
                        }
                    }
                } bind MailSender::class
                single { RecordingEventStoreDB() } bind EventStoreDB::class
                single { SendMailCommandHandler(get(), get()) }
            }

            it("should send mail") {
                withKoin(modules) {
                    val handler = get<SendMailCommandHandler>()
                    val mailSender = get<RecordingMailSender>()
                    val eventStore = get<RecordingEventStoreDB>()

                    val mail = faker.mailModule.mailDto()

                    val result = handler.handleAsync(SendMailCommand(mail))
                    val mailSentEvents =
                        eventStore.readStream(StreamName("${mailAggregateType.type}-${mail.id}")).events

                    expectThat(result).isEqualTo(MailSentResult.Success(mail.id))
                    expectThat(mailSender.hasBeenSentSuccessfully(mail.toDomain())).isTrue()
                    expectThat(eventStore.readStream(StreamName(MailSentSuccessfullyEvent.fullEventType.get())))
                    expectThat(mailSentEvents) {
                        hasSize(1)
                        get { first().event.eventType == MailSentSuccessfullyEvent.fullEventType.get() }.isTrue()
                    }
                }
            }

            it("when sending failed should save MailSendingErrorEvent") {
                withKoin(modules) {
                    val handler = get<SendMailCommandHandler>()
                    val mailSender = get<RecordingMailSender>()
                    val eventStore = get<RecordingEventStoreDB>()

                    val mail = faker.mailModule.mailDto().copy(to = EmailAddress.create("invalid@mail.com"))

                    val result = handler.handleAsync(SendMailCommand(mail))
                    val mailSentEvents =
                        eventStore.readStream(StreamName("${mailAggregateType.type}-${mail.id}")).events

                    expectThat(result).isEqualTo(
                        MailSentResult.Error(
                            mail.id,
                            MailSendingError("Invalid mail address")
                        )
                    )
                    expectThat(mailSender.hasNotBeenSentSuccessfully(mail.toDomain())).isTrue()
                    expectThat(eventStore.readStream(StreamName(MailSendingErrorEvent.fullEventType.get())))
                    expectThat(mailSentEvents) {
                        hasSize(1)
                        get { first().event.eventType == MailSendingErrorEvent.fullEventType.get() }.isTrue()
                    }
                }
            }
        }
    }
}
