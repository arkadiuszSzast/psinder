package com.psinder.mail

class RecordingMailSender(private val mailMapper: (Mail) -> MailSentResult = ::defaultRecordingMailSenderMapper) :
    MailSender {
    private val sentMails: MutableMap<Mail, MailSentResult> = mutableMapOf()

    override suspend fun send(mail: Mail): MailSentResult {
        val result = mailMapper(mail)
        sentMails[mail] = result

        return result
    }

    fun getAll() = sentMails.toMap()
    fun hasBeenSentSuccessfully(mail: Mail) = sentMails[mail] == MailSentResult.Success(mail.id.cast())
    fun hasNotBeenSentSuccessfully(mail: Mail) = sentMails[mail] is MailSentResult.Error
    fun clear() = sentMails.clear()
}

private fun defaultRecordingMailSenderMapper(mail: Mail): MailSentResult {
    return MailSentResult.Success(mail.id.cast())
}
