package com.psinder.auth

data class AuthorityCheckException(override val message: String) : SecurityException(message)
