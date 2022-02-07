package com.psinder.shared.validation

import arrow.core.NonEmptyList
import arrow.typeclasses.Semigroup
import com.psinder.shared.reduce
import io.konform.validation.ValidationError
import io.ktor.features.BadRequestException

data class ValidationException(val validationErrors: NonEmptyList<ValidationError>) :
    BadRequestException(validationErrors.joinToString(",")) {

    companion object {
        val semigroup = object : Semigroup<ValidationException> {
            override fun ValidationException.combine(b: ValidationException): ValidationException {
                return ValidationException(this.validationErrors + b.validationErrors)
            }
        }
    }
}

fun NonEmptyList<ValidationException>.mergeAll() =
    this.reduce(ValidationException.semigroup)
