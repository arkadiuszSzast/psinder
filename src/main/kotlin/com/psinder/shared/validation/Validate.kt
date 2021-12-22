package com.psinder.shared.validation

import arrow.core.*
import com.psinder.shared.TransformableToThrowable
import com.psinder.shared.reduceMap

data class ValidationError(val validationErrorCode: String) : TransformableToThrowable<ValidationException> {
    override fun toThrowable() = ValidationException(validationErrorCode.nel())
}

internal fun NonEmptyList<ValidationError>.mergeToException() =
    this.reduceMap(ValidationException.semigroup) { it.toThrowable() }