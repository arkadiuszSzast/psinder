package com.psinder.account

import kotlinx.serialization.KSerializer
import pl.brightinventions.codified.Codified
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.serializer.codifiedEnumSerializer

enum class LogInFailureError(override val code: String) : Codified<String> {
    AccountSuspended("account_suspended"),
    AccountNotActivated("account_not_activated"),
    InvalidPassword("invalid_password"),
    AccountNotFound("account_not_found");

    object CodifiedSerializer : KSerializer<CodifiedEnum<LogInFailureError, String>> by codifiedEnumSerializer()
}
