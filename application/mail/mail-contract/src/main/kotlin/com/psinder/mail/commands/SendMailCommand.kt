package com.psinder.mail.commands

import com.psinder.mail.MailDto
import com.psinder.mail.MailSentResult
import com.trendyol.kediatr.CommandMetadata
import com.trendyol.kediatr.CommandWithResult

data class SendMailCommand(val mail: MailDto, override val metadata: CommandMetadata? = null) :
    CommandWithResult<MailSentResult>
