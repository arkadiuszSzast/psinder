package com.psinder.shared.validation

import arrow.core.NonEmptyList
import arrow.core.nel
import arrow.typeclasses.Semigroup
import com.psinder.shared.reduce
import io.ktor.features.*

internal data class ValidationException(val validationErrorCodes: NonEmptyList<String>) :
    BadRequestException(validationErrorCodes.joinToString(",")) {

    constructor(errorCode: String): this(errorCode.nel())

    companion object {
        val semigroup = object : Semigroup<ValidationException> {
            override fun ValidationException.combine(b: ValidationException): ValidationException {
                return ValidationException(this.validationErrorCodes + b.validationErrorCodes)
            }
        }
    }
}

internal fun NonEmptyList<ValidationException>.mergeAll() =
    this.reduce(ValidationException.semigroup)
