package com.psinder.account

internal data class LoginRequest(val login: String, val password: String)

internal data class LoginResponse(val token: String)
