package com.psinder.mail.commands

import com.psinder.mail.MailDto
import com.psinder.mail.MailSentResult
import com.trendyol.kediatr.CommandWithResult

data class SendMailCommand(val mail: MailDto) : CommandWithResult<MailSentResult>
