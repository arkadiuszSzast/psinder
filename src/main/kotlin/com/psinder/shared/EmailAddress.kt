package com.psinder.shared

import arrow.core.*
import com.psinder.shared.EmailAddressValidationRules.emailPatternRule
import com.psinder.shared.validation.ValidationError
import com.psinder.shared.validation.ValidationRule
import com.psinder.shared.validation.checkAll

internal data class EmailAddress private constructor(override val value: String) : SimpleValueObject<String> {

    companion object {
        internal fun create(address: String): ValidatedNel<ValidationError, EmailAddress> {
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
        else Some(ValidationError("validation.invalid_email_format"))
    }
}