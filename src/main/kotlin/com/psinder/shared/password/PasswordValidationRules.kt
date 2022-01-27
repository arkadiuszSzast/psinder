package com.psinder.shared.password

import com.psinder.shared.validation.rules.cannotHaveWhitespaces
import com.psinder.shared.validation.rules.containsNumber
import com.psinder.shared.validation.rules.containsSpecialCharacter
import io.konform.validation.Validation
import io.konform.validation.jsonschema.minLength

internal val allowedSpecialCharacters = """!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~""".toList()

private const val DEFAULT_MIN_PASSWORD_LENGTH = 12

internal val defaultPasswordValidator = Validation<String> {
    minLength(DEFAULT_MIN_PASSWORD_LENGTH) hint "validation.password_too_short"
    containsNumber() hint ("validation.password_must_contains_number")
    containsSpecialCharacter() hint ("validation.password_must_contains_special_character")
    cannotHaveWhitespaces() hint ("validation.password_cannot_have_whitespaces")
}
