package com.psinder.auth.authority

import java.util.UUID

sealed class Decision {
    fun toBoolean() = when (this) {
        is Allow -> true
        is Deny -> false
    }

    inline fun onDeny(block: () -> Unit) {
        return when (this) {
            is Deny -> block()
            is Allow -> Unit
        }
    }
}

data class Allow(val id: UUID) : Decision()
data class Deny(val reason: Throwable) : Decision()
