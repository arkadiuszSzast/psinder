package com.psinder.account.responses

import com.psinder.shared.jwt.JwtToken

internal data class LoginAccountResponse(val token: JwtToken)
