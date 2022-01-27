package com.psinder.shared

import com.psinder.shared.validation.Validatable
import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
internal value class EmailAddress private constructor(val value: String) : Validatable<EmailAddress> {

    companion object {
        internal fun create(address: String) = EmailAddress(address.trim())

        val validator = Validation<EmailAddress> {
            EmailAddress::value {
                pattern(EMAIL_PATTERN) hint "validation.invalid_email_format"
            }
        }
    }

    override val validator: Validation<EmailAddress>
        get() = EmailAddress.validator
}
