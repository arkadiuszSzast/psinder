package com.psinder.account.activation.requests

import com.psinder.shared.jwt.JwtToken
import com.psinder.shared.validation.Validatable
import io.konform.validation.Validation
import kotlinx.serialization.Serializable

@Serializable
data class ActivateAccountRequest(val token: JwtToken) : Validatable<ActivateAccountRequest> {

    companion object {
        val validator = Validation<ActivateAccountRequest> {
            ActivateAccountRequest::token {
                run(JwtToken.validator)
            }
        }
    }

    override val validator: Validation<ActivateAccountRequest>
        get() = ActivateAccountRequest.validator
}
