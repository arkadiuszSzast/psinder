package com.psinder.mail

import com.psinder.shared.EmailAddress

data class MailProperties(val templateId: MailTemplateId, val subject: MailSubject, val sender: EmailAddress)
