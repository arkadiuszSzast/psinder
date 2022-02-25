package com.psinder.auth.principal

import com.psinder.auth.account.AccountContext
import com.psinder.auth.account.AccountId
import com.psinder.auth.role.Role
import pl.brightinventions.codified.enums.CodifiedEnum

class SimpleAccountContext(
    override val accountId: AccountId,
    override val role: CodifiedEnum<Role, String>
) : AccountContext
