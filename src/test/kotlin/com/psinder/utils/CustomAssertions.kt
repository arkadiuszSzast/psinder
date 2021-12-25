package com.psinder.utils

import arrow.integrations.jackson.module.registerArrowModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.psinder.shared.toObject
import io.ktor.server.testing.*
import strikt.api.Assertion

private inline fun <reified T> Assertion.Builder<TestApplicationResponse>.contentIsTypeOf() {
    assert("Request content can be deserialized to: ${T::class.qualifiedName}") {
        with(it.content) {
            when (this) {
                null -> fail("Cannot construct [${T::class.qualifiedName}] from null.")
                else -> {
                    val result = jacksonObjectMapper().registerArrowModule().toObject<T>(this) //TODO: provide ObjectMapper
                    result.fold(
                        { fail("Cannot construct [${T::class.qualifiedName}] from given string: $this. Error: ${it.message}") },
                        { pass() }
                    )
                }
            }
        }
    }
}
