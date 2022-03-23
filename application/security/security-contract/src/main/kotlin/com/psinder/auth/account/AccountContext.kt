package com.psinder.auth.account

import com.psinder.auth.role.HasRole
import com.psinder.auth.role.Role
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.codifiedEnum

interface AccountContext : AccountIdProvider, HasRole {
    companion object {
        val unknown = object : AccountContext {
            override val accountId: AccountId = AccountId("unknown")
            override val role: CodifiedEnum<Role, String> = "unknown".codifiedEnum()
        }
    }
}
