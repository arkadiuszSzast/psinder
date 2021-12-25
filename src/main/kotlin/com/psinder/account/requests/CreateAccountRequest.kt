package com.psinder.account.requests

import arrow.core.*
import com.fasterxml.jackson.annotation.JsonCreator
import com.psinder.account.Username
import com.psinder.shared.EmailAddress
import com.psinder.shared.password.Password
import com.psinder.shared.validation.ValidationException
import com.psinder.shared.validation.mergeAll

internal data class CreateAccountRequest private constructor(
    val username: Username,
    val emailAddress: EmailAddress,
    val password: Password,
) {
    companion object {
        internal fun create(username: String, email: String, password: String): ValidatedNel<ValidationException, CreateAccountRequest> {
            return Username.create(username).zip(
                EmailAddress.create(email),
                Password.create(password)
            ) { username, email, password -> CreateAccountRequest(username, email, password) }
        }

        @JvmStatic
        @JsonCreator
        internal fun createOrThrow(username: String, email: String, password: String): CreateAccountRequest =
            when (val vRegisterRequest = create(username, email, password)) {
                is Invalid -> throw vRegisterRequest.value.mergeAll()
                is Valid -> vRegisterRequest.value
            }
    }
}
