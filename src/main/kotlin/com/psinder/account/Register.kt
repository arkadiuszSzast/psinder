package com.psinder.account

import arrow.core.*
import com.fasterxml.jackson.annotation.JsonCreator
import com.psinder.shared.*
import com.psinder.shared.password.Password
import com.psinder.shared.validation.ValidationException
import com.psinder.shared.validation.mergeAll

internal data class RegisterRequest private constructor(
    val username: Username,
    val emailAddress: EmailAddress,
    val password: Password,
) {
    companion object {
        internal fun create(username: String, email: String, password: String): ValidatedNel<ValidationException, RegisterRequest> {
            return Username.create(username).zip(
                EmailAddress.create(email),
                Password.create(password)
            ) { username, email, password -> RegisterRequest(username, email, password) }
        }

        @JvmStatic
        @JsonCreator
        internal fun createOrThrow(username: String, email: String, password: String): RegisterRequest =
            when (val vRegisterRequest = create(username, email, password)) {
                is Invalid -> throw vRegisterRequest.value.mergeAll()
                is Valid -> vRegisterRequest.value
            }
    }

}

internal data class RegisterResponse(val username: String, val emailAddress: EmailAddress)
