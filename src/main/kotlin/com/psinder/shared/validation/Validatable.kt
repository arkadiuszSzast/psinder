package com.psinder.shared.validation

import arrow.core.Nel
import arrow.core.NonEmptyList
import arrow.core.invalidNel
import arrow.core.validNel
import io.konform.validation.Invalid
import io.konform.validation.Valid
import io.konform.validation.Validation

internal interface Validatable<T> {
    val validator: Validation<T>
}

internal inline fun <reified T : Validatable<T>> T.validate() = when (val result = validator.validate(this)) {
    is Valid -> this.validNel()
    is Invalid -> ValidationException(NonEmptyList.fromListUnsafe(result.errors)).invalidNel()
}

internal inline fun <reified T : Validatable<T>> T.validate(customValidator: Validation<T>) =
    when (val result = customValidator.validate(this)) {
        is Valid -> this.validNel()
        is Invalid -> ValidationException(NonEmptyList.fromListUnsafe(result.errors)).invalidNel()
    }

internal inline fun <reified T : Validatable<T>> T.validateEagerly() = when (val result = validator.validate(this)) {
    is Invalid -> throw ValidationException(Nel.fromListUnsafe(result.errors))
    is Valid -> this
}

internal inline fun <reified T : Validatable<T>> T.validateEagerly(customValidator: Validation<T>) =
    when (val result = customValidator.validate(this)) {
        is Invalid -> throw ValidationException(Nel.fromListUnsafe(result.errors))
        is Valid -> this
    }
