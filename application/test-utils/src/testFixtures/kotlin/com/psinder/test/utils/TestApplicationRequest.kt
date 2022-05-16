package com.psinder.test.utils

import io.ktor.server.testing.TestApplicationRequest
import io.ktor.server.testing.setBody
import kotlinx.serialization.encodeToString

inline fun <reified T> TestApplicationRequest.setBody(value: T) {
    val mapper = JsonMapper.defaultMapper
    setBody(mapper.encodeToString(value))
}
