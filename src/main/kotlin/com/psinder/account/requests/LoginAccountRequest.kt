package com.psinder.account.requests

import com.psinder.shared.EmailAddress
import com.psinder.shared.password.RawPassword

internal data class LoginAccountRequest(val email: EmailAddress, val password: RawPassword)
