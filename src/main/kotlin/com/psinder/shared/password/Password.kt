package com.psinder.shared.password

import arrow.core.Nel
import arrow.core.Valid
import arrow.core.ValidatedNel
import arrow.core.invalid
import com.psinder.shared.validation.ValidationException
import com.psinder.shared.validation.ValidationRule
import com.psinder.shared.validation.checkAll
import org.mindrot.jbcrypt.BCrypt

@JvmInline
internal value class Password private constructor(val value: String) {

    companion object {
        internal fun create(
            password: String,
            rules: List<ValidationRule<String>> = defaultPasswordValidationRules
        ): ValidatedNel<ValidationException, Password> {
            val errors = rules.checkAll(password)

            return if (errors.isNotEmpty()) {
                Nel.fromListUnsafe(errors).invalid()
            } else {
                Valid(Password(BCrypt.hashpw(password, BCrypt.gensalt())))
            }
        }
    }
}
