package com.psinder.account

import pl.brightinventions.codified.Codified
import pl.brightinventions.codified.enums.codifiedEnum

enum class AccountStatus(override val code: String) : Codified<String> {
    ACTIVE("Active"),
    WAITING_FOR_ACTIVATION("Waiting_for_activation"),
    SUSPENDED("Suspended")
}

val cod = AccountStatus.ACTIVE.codifiedEnum()
