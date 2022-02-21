package com.psinder.auth.account

import com.psinder.auth.role.HasRole
import com.psinder.shared.EmailAddress

interface AccountContext : AccountIdProvider, HasRole {
    val email: EmailAddress
}
