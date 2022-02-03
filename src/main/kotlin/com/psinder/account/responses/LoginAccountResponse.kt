package com.psinder.account.responses

import com.psinder.shared.jwt.JwtToken

@kotlinx.serialization.Serializable
internal data class LoginAccountResponse(val token: JwtToken)
