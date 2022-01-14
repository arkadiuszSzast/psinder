package com.psinder.account.requests

import arrow.core.Invalid
import arrow.core.Valid
import arrow.core.zip
import com.psinder.account.PersonalData
import com.psinder.shared.EmailAddress
import com.psinder.shared.password.Password
import com.psinder.shared.validation.mergeAll
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateAccountRequest private constructor(
    val personalData: PersonalData,
    val email: EmailAddress,
    val password: Password,
    val timeZoneId: TimeZone
) {
    companion object {
        internal fun create(personalData: PersonalData, email: String, password: String, timeZoneId: TimeZone) =
            EmailAddress.create(email).zip(Password.create(password)) { email, password ->
                CreateAccountRequest(personalData, email, password, timeZoneId)
            }

        internal fun createOrThrow(personalData: PersonalData, email: String, password: String, timeZoneId: TimeZone) =
            when (val vRegisterRequest = create(personalData, email, password, timeZoneId)) {
                is Invalid -> throw vRegisterRequest.value.mergeAll()
                is Valid -> vRegisterRequest.value
            }
    }
}
