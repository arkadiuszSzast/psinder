package com.psinder.shared.validation.rules

import com.psinder.shared.validation.allowedSpecialCharacters
import io.konform.validation.Constraint
import io.konform.validation.ValidationBuilder

fun ValidationBuilder<String>.containsNumber(): Constraint<String> = addConstraint(
    "must contains a number"
) {
    val numbersPattern = """([0-9])"""
    numbersPattern.toRegex().containsMatchIn(it)
}

fun ValidationBuilder<String>.containsSpecialCharacter(): Constraint<String> =
    addConstraint(
        "must contains a special character"
    ) {
        val specialCharactersPattern = """([${allowedSpecialCharacters.joinToString(",")}])"""
        specialCharactersPattern.toRegex().containsMatchIn(it)
    }

fun ValidationBuilder<String>.cannotStartsWith(value: String): Constraint<String> = addConstraint(
    "cannot starts with {0}",
    value
) {
    it.startsWith(value).not()
}

fun ValidationBuilder<String>.cannotEndsWith(value: String): Constraint<String> = addConstraint(
    "cannot ends with {0}",
    value
) {
    it.endsWith(value).not()
}

fun ValidationBuilder<String>.cannotHaveWhitespaces(): Constraint<String> = addConstraint(
    "cannot contains whitespace character"
) {
    val whitespacesPattern = """\s"""
    whitespacesPattern.toRegex().containsMatchIn(it).not()
}
