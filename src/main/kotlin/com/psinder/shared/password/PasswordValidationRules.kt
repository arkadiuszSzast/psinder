package com.psinder.shared.password

import arrow.core.nonEmptyListOf
import com.psinder.shared.validation.rules.StringValidationRules

internal val allowedSpecialCharacters = """!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~""".toList()

private const val DEFAULT_MIN_PASSWORD_LENGTH = 12L

internal val defaultPasswordValidationRules = nonEmptyListOf(
    StringValidationRules.nonBlankRule("validation.password_cannot_be_blank"),
    StringValidationRules.minLengthRule(DEFAULT_MIN_PASSWORD_LENGTH, "validation.password_too_short"),
    StringValidationRules.containsNumberRule("validation.password_must_contains_number"),
    StringValidationRules.containsSpecialCharacterRule("validation.password_must_contains_special_character"),
    StringValidationRules.cannotHaveWhitespacesRule("validation.password_cannot_have_whitespaces"),
)
