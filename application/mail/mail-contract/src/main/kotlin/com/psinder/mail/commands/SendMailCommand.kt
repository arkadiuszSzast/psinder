package com.psinder.mail.commands

import com.psinder.mail.Mail
import com.psinder.mail.MailSentResult
import com.trendyol.kediatr.CommandWithResult

data class SendMailCommand(val mail: Mail) : CommandWithResult<MailSentResult>
