package com.psinder.shared.validation

import arrow.core.NonEmptyList
import arrow.typeclasses.Semigroup

data class ValidationException(val validationErrorCodes: NonEmptyList<String>) :
    RuntimeException(validationErrorCodes.joinToString(",")) {

    companion object {
        val semigroup = object : ValidationExceptionMonoid {}
    }
}

interface ValidationExceptionMonoid : Semigroup<ValidationException> {
    override fun ValidationException.combine(b: ValidationException): ValidationException {
        return ValidationException(this.validationErrorCodes + b.validationErrorCodes)
    }
}
