package com.psinder.plugins

import arrow.core.NonEmptyList
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException
import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.psinder.shared.rootCause
import com.psinder.shared.validation.ValidationException
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.response.respond

internal fun Application.configureExceptionsHandling() {
    install(StatusPages) {
        exception<ValueInstantiationException> { exception ->
            with(exception.rootCause) {
                when (this) {
                    is ValidationException -> call.respond(
                        BadRequest,
                        ValidationErrorMessage(this.validationErrorCodes, this::class.java.simpleName)
                    )
                    else -> call.respond(InternalServerError, exception.rootCause.createHttpErrorMessage())
                }
            }
        }
        exception<MissingKotlinParameterException>() {
            call.respond(BadRequest, it.rootCause.createHttpErrorMessage())
        }
        exception<InvalidDefinitionException> {
            call.respond(InternalServerError, it.rootCause.createHttpErrorMessage())
        }
    }
}

internal fun Throwable.createHttpErrorMessage(): HttpError =
    when (this) {
        is ValidationException -> ValidationErrorMessage(this.validationErrorCodes, this::class.java.simpleName)
        else -> GenericErrorMessage(this.message ?: "UNKNOWN_ERROR", this::class.java.simpleName)
    }

internal sealed interface HttpError {
    val type: String

    interface HttpErrorSingleMessage : HttpError {
        val message: String
    }

    interface HttpErrorArrayMessage : HttpError {
        val message: NonEmptyList<String>
    }
}

internal data class ValidationErrorMessage(override val message: NonEmptyList<String>, override val type: String) :
    HttpError.HttpErrorArrayMessage

internal data class GenericErrorMessage(override val message: String, override val type: String) :
    HttpError.HttpErrorSingleMessage
