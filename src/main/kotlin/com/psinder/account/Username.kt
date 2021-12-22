package com.psinder.account

import arrow.core.*
import com.psinder.shared.SimpleValueObject
import com.psinder.shared.validation.ValidationError
import com.psinder.shared.validation.checkAll
import com.psinder.shared.validation.rules.StringValidationRules

internal data class Username(override val value: String) : SimpleValueObject<String> {

    companion object {
        internal fun create(username: String): ValidatedNel<ValidationError, Username> {
            val errors = validationRules.checkAll(username)

            return if (errors.isNotEmpty()) {
                Nel.fromListUnsafe(errors).invalid()
            } else {
                Valid(Username(username.trim()))
            }
        }

        private val validationRules = listOf(StringValidationRules.nonBlankRule("validation.blank_username"))
    }
}
