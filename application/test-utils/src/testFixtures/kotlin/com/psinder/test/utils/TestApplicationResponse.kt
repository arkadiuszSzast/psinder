package com.psinder.test.utils

import io.ktor.server.testing.TestApplicationResponse
import kotlinx.serialization.decodeFromString

inline fun <reified T> TestApplicationResponse.contentAs(): T {
    val mapper = JsonMapper.defaultMapper
    assert(content != null) { "Response content is null" }
    return mapper.decodeFromString(this.content!!)
}
