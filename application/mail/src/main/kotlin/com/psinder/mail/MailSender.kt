package com.psinder.mail

interface MailSender {
    suspend fun send(mail: MailDto): MailSentResult
}
