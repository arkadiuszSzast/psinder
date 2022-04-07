package com.psinder.mail

import com.psinder.mail.events.MailSendingErrorEvent
import com.psinder.mail.events.MailSentSuccessfullyEvent
import com.psinder.shared.EmailAddress
import com.psinder.test.utils.faker
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isA

class MailTest : DescribeSpec() {

    private val mailSender = RecordingMailSender {
        when (it.to) {
            EmailAddress.create("invalid@mail.com") ->
                MailSentResult.Error(it.id.cast(), MailSendingError("Invalid mail address"))
            else -> MailSentResult.Success(it.id.cast())
        }
    }

    init {

        beforeEach {
            mailSender.clear()
        }

        describe("send mail") {

            it("should return mail MailSentSuccessfullyEvent") {
                // arrange
                val mail = faker.mailModule.mail()

                // act
                val result = Mail.send(mailSender, mail.toDto())

                // assert
                expectThat(result) {
                    isA<MailSentSuccessfullyEvent>()
                }
            }

            it("should return mail MailSendingErrorEvent") {
                val mail = faker.mailModule.mail().copy(to = EmailAddress.create("invalid@mail.com"))

                val result = Mail.send(mailSender, mail.toDto())

                expectThat(result) {
                    isA<MailSendingErrorEvent>()
                }
            }
        }
    }
}
