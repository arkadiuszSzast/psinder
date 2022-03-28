package com.psinder.shared.response

import com.psinder.shared.isBetween

@JvmInline
value class ResponseCode(val code: Int) {
    val isSuccess
        get() = code.isBetween(200, 299)
}
