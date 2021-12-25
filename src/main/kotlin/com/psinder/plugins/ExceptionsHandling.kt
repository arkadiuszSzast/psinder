package com.psinder.plugins

import arrow.core.NonEmptyList
import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import com.psinder.shared.rootCause
import com.psinder.shared.validation.ValidationException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

fun Application.configureExceptionsHandling() {
    install(StatusPages) {
        exception<ValueInstantiationException> { exception ->
            when (exception.rootCause) {
                is ValidationException -> call.respond(HttpStatusCode.BadRequest, exception.rootCause.createHttpCustomErrorMessage())
                else -> call.respond(HttpStatusCode.InternalServerError, exception.rootCause.createHttpCustomErrorMessage())
            }
        }
    }
}

internal fun Throwable.createHttpCustomErrorMessage(): HttpError =
    when (this) {
        is ValidationException -> ValidationErrorMessage(this.validationErrorCodes, this::class.java.simpleName)
        else -> GenericErrorMessage(this.message ?: "UNKNOWN_ERROR", this::class.java.simpleName)
    }

sealed interface HttpError {
    val type: String

    interface HttpErrorSingleMessage : HttpError {
        val message: String
    }

    interface HttpErrorArrayMessage : HttpError {
        val message: NonEmptyList<String>
    }
}

internal data class ValidationErrorMessage(override val message: NonEmptyList<String>, override val type: String) : HttpError.HttpErrorArrayMessage
internal data class GenericErrorMessage(override val message: String, override val type: String) : HttpError.HttpErrorSingleMessage
