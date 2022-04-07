package com.psinder.mail

import com.psinder.shared.response.ResponseCode
import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.objects.Email
import com.sendgrid.helpers.mail.objects.Personalization
import com.sendgrid.helpers.mail.Mail as SendGridMail

internal class SendGridMailSender(private val sendGridClient: SendGrid) : MailSender {
    override suspend fun send(mail: MailDto): MailSentResult {
        val sendGridMail = mail.toSendgridMail()

        val request = Request().apply {
            method = Method.POST
            endpoint = "mail/send"
            body = sendGridMail.build()
        }

        val response = sendGridClient.api(request)
        val statusCode = ResponseCode(response.statusCode)

        return when {
            statusCode.isSuccess -> MailSentResult.Success(mail.id.cast())
            else -> MailSentResult.Error(mail.id.cast(), MailSendingError(response.body))
        }
    }

    private fun MailDto.toSendgridMail(): SendGridMail {
        val mail = this
        val personalization = Personalization().apply {
            addTo(Email(mail.to.value))
            mail.variables.variables.forEach { addDynamicTemplateData(it.key, it.value) }
        }

        return SendGridMail().apply {
            from = Email(mail.from.value)
            templateId = mail.templateId.id
            addPersonalization(personalization)
        }
    }
}
