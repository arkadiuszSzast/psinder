package com.psinder.plugins

import arrow.core.NonEmptyList
import com.psinder.shared.rootCause
import com.psinder.shared.validation.ValidationException
import io.konform.validation.ValidationError
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.response.respond
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException

internal fun Application.configureExceptionsHandling() {
    install(StatusPages) {
        exception<ValidationException>() {
            call.respond(
                BadRequest,
                ValidationErrorMessage(it.validationErrors.toInternalValidationCodes(), it::class.java.simpleName)
            )
        }
        exception<SerializationException> {
            call.respond(InternalServerError, it.rootCause.createHttpErrorMessage())
        }
    }
}

internal fun Throwable.createHttpErrorMessage(): HttpError =
    when (this) {
        is ValidationException -> ValidationErrorMessage(
            this.validationErrors.toInternalValidationCodes(),
            this::class.java.simpleName
        )
        else -> GenericErrorMessage(this.message ?: "UNKNOWN_ERROR", this::class.java.simpleName)
    }

internal fun NonEmptyList<ValidationError>.toInternalValidationCodes() =
    this.map { InternalValidationError(it.dataPath, it.message) }.toList()

internal sealed interface HttpError {
    val type: String

    interface HttpErrorSingleMessage : HttpError {
        val message: String
    }

    interface HttpValidationErrorMessage : HttpError {
        val message: List<InternalValidationError>
    }
}

@Serializable
internal data class ValidationErrorMessage(
    override val message: List<InternalValidationError>,
    override val type: String
) :
    HttpError.HttpValidationErrorMessage

@Serializable
internal data class GenericErrorMessage(override val message: String, override val type: String) :
    HttpError.HttpErrorSingleMessage

@Serializable
internal data class InternalValidationError(
    val dataPath: String,
    val message: String
)
