package com.psinder.shared

import arrow.core.Nel
import arrow.core.None
import arrow.core.Some
import arrow.core.Valid
import arrow.core.ValidatedNel
import arrow.core.invalid
import com.psinder.shared.EmailAddressValidationRules.emailPatternRule
import com.psinder.shared.validation.ValidationException
import com.psinder.shared.validation.ValidationRule
import com.psinder.shared.validation.checkAll

@JvmInline
internal value class EmailAddress private constructor(val value: String) {

    companion object {
        internal fun create(address: String): ValidatedNel<ValidationException, EmailAddress> {
            val errors = validationRules.checkAll(address)

            return if (errors.isNotEmpty()) {
                Nel.fromListUnsafe(errors).invalid()
            } else {
                Valid(EmailAddress(address.trim()))
            }
        }

        private val validationRules = listOf(emailPatternRule)
    }
}

private object EmailAddressValidationRules {
    val emailPatternRule = ValidationRule<String> {
        if (it.matches(emailPattern.toRegex())) None
        else Some(ValidationException("validation.invalid_email_format"))
    }
}
