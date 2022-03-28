package com.psinder.mail

import com.psinder.shared.EmailAddress
import com.psinder.shared.validation.Validatable
import io.konform.validation.Validation
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class Mail(
    @Contextual val id: Id<Mail>,
    val subject: MailSubject,
    val from: EmailAddress,
    val to: EmailAddress,
    val templateId: MailTemplateId,
    val variables: MailVariables
) : Validatable<Mail> {

    constructor(
        subject: MailSubject,
        from: EmailAddress,
        to: EmailAddress,
        templateId: MailTemplateId,
        variables: MailVariables
    ) : this(newId(), subject, from, to, templateId, variables)

    companion object {
        val validator = Validation<Mail> {
            Mail::from {
                run(EmailAddress.validator)
            }
            Mail::to {
                run(EmailAddress.validator)
            }
        }
    }

    override val validator: Validation<Mail>
        get() = Mail.validator
}
