package com.psinder.mail

import com.psinder.shared.EmailAddress
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class MailDto(
    @Contextual val id: Id<MailDto>,
    val subject: MailSubject,
    val from: EmailAddress,
    val to: EmailAddress,
    val templateId: MailTemplateId,
    val variables: MailVariables
) {

    constructor(
        subject: MailSubject,
        from: EmailAddress,
        to: EmailAddress,
        templateId: MailTemplateId,
        variables: MailVariables
    ) : this(newId(), subject, from, to, templateId, variables)

    companion object
}
