package com.psinder.account.responses

import com.psinder.shared.jwt.JwtToken
import kotlinx.serialization.Serializable

@Serializable
data class LoginAccountResponse(val token: JwtToken)
