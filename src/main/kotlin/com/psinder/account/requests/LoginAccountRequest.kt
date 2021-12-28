package com.psinder.account.requests

import com.psinder.shared.EmailAddress
import com.psinder.shared.password.Password

internal data class LoginAccountRequest(val email: EmailAddress, val password: Password)
