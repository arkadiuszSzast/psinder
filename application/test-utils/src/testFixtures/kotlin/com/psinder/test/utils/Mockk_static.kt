package com.psinder.test.utils

import io.mockk.mockkStatic

fun mockKtorInstallFunction() = mockkStatic("io.ktor.application.ApplicationFeatureKt")
fun mockConfigureAccountRouting() = mockkStatic("com.psinder.account.RoutingKt")
