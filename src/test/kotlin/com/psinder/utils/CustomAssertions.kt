package com.psinder.utils

import com.psinder.shared.json.JsonMapper
import com.psinder.shared.json.decodeFromString
import io.ktor.server.testing.TestApplicationResponse
import strikt.api.Assertion

private inline fun <reified T> Assertion.Builder<TestApplicationResponse>.contentIsTypeOf() {
    assert("Request content can be deserialized to: ${T::class.qualifiedName}") {
        with(it.content) {
            when (this) {
                null -> fail("Cannot construct [${T::class.qualifiedName}] from null.")
                else -> {
                    val result = JsonMapper.defaultMapper.decodeFromString<T>(this)
                    result.fold(
                        { fail("Cannot construct [${T::class.qualifiedName}] from given string: $this. Error: ${it.message}") },
                        { pass() }
                    )
                }
            }
        }
    }
}
