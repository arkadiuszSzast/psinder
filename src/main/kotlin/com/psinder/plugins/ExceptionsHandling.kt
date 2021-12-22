package com.psinder.plugins

import arrow.core.NonEmptyList
import com.psinder.shared.rootCause
import com.psinder.shared.validation.ValidationException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

fun Application.configureExceptionsHandling() {
    install(StatusPages) {
        exception<BadRequestException> { exception ->
            call.respond(HttpStatusCode.BadRequest, exception.rootCause.createHttpCustomErrorMessage())
        }
    }
}

internal fun Throwable.createHttpCustomErrorMessage() =
    when (this) {
        is ValidationException -> ValidationErrorMessage(this.validationErrorCodes, this::class.java.simpleName)
        else -> GenericErrorMessage(this.message ?: "UNKNOWN_ERROR", this::class.java.simpleName)
    }

internal interface HttpErrorMessage {
    val type: String
}

internal data class ValidationErrorMessage(val validationErrorCodes: NonEmptyList<String>, override val type: String) : HttpErrorMessage
internal data class GenericErrorMessage(val errorMessage: String, override val type: String) : HttpErrorMessage