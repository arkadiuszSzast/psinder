package com.psinder.shared.validation.rules

import arrow.core.None
import arrow.core.Some
import com.psinder.shared.emailPattern
import com.psinder.shared.password.allowedSpecialCharacters
import com.psinder.shared.validation.ValidationException
import com.psinder.shared.validation.ValidationRule

object StringValidationRules {

    fun nonEmptyRule(errorMessage: String = "validation.string_empty") = ValidationRule<String> {
        if(it.isNotEmpty()) None
        else Some(ValidationException(errorMessage))
    }

    fun nonBlankRule(errorMessage: String = "validation.string_blank") = ValidationRule<String> {
        if(it.isNotBlank()) None
        else Some(ValidationException(errorMessage))
    }

    fun minLengthRule(minLength: Long, errorMessage: String = "validation.string_too_short") = ValidationRule<String> {
        if(it.length >= minLength) None
        else Some(ValidationException(errorMessage))
    }

    fun containsNumberRule(errorMessage: String = "validation.string_missing_number") = ValidationRule<String> {
        val numbersPattern = """([0-9])"""
        if(numbersPattern.toRegex().containsMatchIn(it)) None
        else Some(ValidationException(errorMessage))
    }

    fun containsSpecialCharacterRule(errorMessage: String = "validation.string_missing_special_character") = ValidationRule<String> {
        val specialCharactersPattern = """([${allowedSpecialCharacters.joinToString()}])"""
        if(specialCharactersPattern.toRegex().containsMatchIn(it)) None
        else Some(ValidationException(errorMessage))
    }

    fun cannotStartWithRule(value: String, errorMessage: String = "validation.string_cannot_starts_with") = ValidationRule<String> {
        if(it.startsWith(value).not()) None
        else Some(ValidationException(errorMessage))
    }

    fun cannotEndsWithRule(value: String, errorMessage: String = "validation.string_cannot_ends_with") = ValidationRule<String> {
        if(it.endsWith(value).not()) None
        else Some(ValidationException(errorMessage))
    }

    fun cannotHaveWhitespacesRule(errorMessage: String = "validation.string_cannot_have_whitespaces") = ValidationRule<String> {
        val whitespacesPattern = """\s"""
        if(whitespacesPattern.toRegex().containsMatchIn(it)) Some(ValidationException(errorMessage))
        else None
    }

    fun isValidEmailRule(errorMessage: String = "validation.invalid_email_format") = ValidationRule<String> {
        if (it.matches(emailPattern.toRegex())) None
        else Some(ValidationException(errorMessage))
    }
}
