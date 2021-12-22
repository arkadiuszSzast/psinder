package com.psinder.shared.password

import arrow.core.*
import com.psinder.shared.validation.ValidationError
import com.psinder.shared.validation.ValidationRule
import com.psinder.shared.validation.checkAll
import org.mindrot.jbcrypt.BCrypt

internal data class Password private constructor(val hashedPassword: String) {

    companion object {
        internal fun create(password: String, rules: List<ValidationRule<String>> = defaultPasswordValidationRules): ValidatedNel<ValidationError, Password> {
            val errors = rules.checkAll(password)

            return if (errors.isNotEmpty()) {
                Nel.fromListUnsafe(errors).invalid()
            } else {
                Valid(Password(BCrypt.hashpw(password, BCrypt.gensalt())))
            }
        }
    }
}

