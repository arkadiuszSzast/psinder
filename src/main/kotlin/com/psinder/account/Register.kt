package com.psinder.account

import arrow.core.*
import com.fasterxml.jackson.annotation.JsonCreator
import com.psinder.shared.*
import com.psinder.shared.password.Password
import com.psinder.shared.validation.ValidationError
import com.psinder.shared.validation.mergeToException

internal data class RegisterRequest private constructor(
    val username: Username,
    val emailAddress: EmailAddress,
    val password: Password,
) {
    companion object {
        internal fun create(username: String, email: String, password: String): ValidatedNel<ValidationError, RegisterRequest> {
            return Username.create(username).zip(
                EmailAddress.create(email),
                Password.create(password)
            ) { username, email, password -> RegisterRequest(username, email, password) }
        }

        @JvmStatic
        @JsonCreator
        internal fun createOrThrow(username: String, email: String, password: String): RegisterRequest =
            when (val vRegisterRequest = create(username, email, password)) {
                is Invalid -> throw vRegisterRequest.value.mergeToException()
                is Valid -> vRegisterRequest.value

            }
    }

}

internal data class RegisterResponse(val username: String, val emailAddress: EmailAddress)
