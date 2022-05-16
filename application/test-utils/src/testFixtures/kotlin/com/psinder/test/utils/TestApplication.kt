package com.psinder.test.utils

import io.ktor.application.Application
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.createTestEnvironment
import io.ktor.server.testing.withApplication
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

suspend fun <R> withTestApplicationSuspending(
    moduleFunction: Application.() -> Unit,
    test: suspend TestApplicationEngine.() -> R
) = withContext(coroutineContext) {
    withApplication(createTestEnvironment()) {
        moduleFunction(application)
        runBlocking { test() }
    }
}
