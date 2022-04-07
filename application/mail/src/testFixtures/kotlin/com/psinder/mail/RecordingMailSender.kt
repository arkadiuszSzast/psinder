package com.psinder.mail

class RecordingMailSender(private val mailMapper: (MailDto) -> MailSentResult = ::defaultRecordingMailSenderMapper) :
    MailSender {
    private val sentMails: MutableMap<MailDto, MailSentResult> = mutableMapOf()

    override suspend fun send(mail: MailDto): MailSentResult {
        val result = mailMapper(mail)
        sentMails[mail] = result

        return result
    }

    fun getAll() = sentMails.toMap()
    fun hasBeenSentSuccessfully(mail: MailDto) = sentMails[mail] == MailSentResult.Success(mail.id.cast())
    fun hasNotBeenSentSuccessfully(mail: MailDto) = sentMails[mail] is MailSentResult.Error
    fun clear() = sentMails.clear()
}

private fun defaultRecordingMailSenderMapper(mail: MailDto): MailSentResult {
    return MailSentResult.Success(mail.id.cast())
}
