package com.psinder.account.requests

import com.psinder.account.PersonalData
import com.psinder.shared.EmailAddress
import com.psinder.shared.password.RawPassword
import com.psinder.shared.validation.Validatable
import io.konform.validation.Validation
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateAccountRequest constructor(
    val personalData: PersonalData,
    val email: EmailAddress,
    val password: RawPassword,
    val timeZoneId: TimeZone
) : Validatable<CreateAccountRequest> {

    companion object {
        val validator = Validation<CreateAccountRequest> {
            CreateAccountRequest::password {
                run(RawPassword.validator)
            }
            CreateAccountRequest::email {
                run(EmailAddress.validator)
            }
        }
    }

    override val validator: Validation<CreateAccountRequest>
        get() = CreateAccountRequest.validator
}
