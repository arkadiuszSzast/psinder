package com.psinder.auth

import com.psinder.shared.EmailAddress

interface AccountContext : AccountIdProvider, HasRole {
    val email: EmailAddress
}
