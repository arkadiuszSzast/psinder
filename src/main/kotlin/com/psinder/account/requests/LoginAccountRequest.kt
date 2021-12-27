package com.psinder.account.requests

import com.psinder.account.Username
import com.psinder.shared.password.Password

internal data class LoginAccountRequest(val login: Username, val password: Password)
