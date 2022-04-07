package com.psinder.shared.jwt

data class TokenMissingSubjectException(override val message: String = "No subject claim in token") :
    RuntimeException(message)
