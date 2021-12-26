package com.psinder.account

import arrow.core.Nel
import arrow.core.Valid
import arrow.core.ValidatedNel
import arrow.core.invalid
import com.psinder.shared.validation.ValidationException
import com.psinder.shared.validation.checkAll
import com.psinder.shared.validation.rules.StringValidationRules

@JvmInline
internal value class Username private constructor(val value: String) {

    companion object {
        internal fun create(username: String): ValidatedNel<ValidationException, Username> {
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
