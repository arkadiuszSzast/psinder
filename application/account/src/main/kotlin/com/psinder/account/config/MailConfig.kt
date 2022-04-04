package com.psinder.account.config

import com.psinder.mail.MailProperties
import com.psinder.mail.MailSubject
import com.psinder.mail.MailTemplateId
import com.psinder.shared.EmailAddress
import com.psinder.shared.config.ConfigKey
import com.psinder.shared.config.getProperty
import com.psinder.shared.validation.validateEagerly

internal object MailConfig {
    val activateAccount by lazy {
        val templateId = getProperty(Keys.activateAccount + Keys.templateId)
        val sender = getProperty(Keys.activateAccount + Keys.sender)
        val subject = getProperty(Keys.activateAccount + Keys.subject)

        MailProperties(
            MailTemplateId(templateId),
            MailSubject(subject),
            EmailAddress.create(sender).validateEagerly()
        )
    }

    private object Keys {
        val templateId = ConfigKey("templateId")
        val sender = ConfigKey("sender")
        val subject = ConfigKey("subject")
        val activateAccount = ConfigKey("mail.activateAccount")
    }
}
