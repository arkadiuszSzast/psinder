package com.psinder.account.requests

import com.psinder.shared.EmailAddress
import com.psinder.shared.password.RawPassword
import com.psinder.shared.validation.Validatable
import io.konform.validation.Validation
import kotlinx.serialization.Serializable

@Serializable
data class LoginAccountRequest(val email: EmailAddress, val password: RawPassword) :
    Validatable<LoginAccountRequest> {

    companion object {
        val validator = Validation<LoginAccountRequest> {
            LoginAccountRequest::email {
                run(EmailAddress.validator)
            }
        }
    }

    override val validator: Validation<LoginAccountRequest>
        get() = LoginAccountRequest.validator
}
