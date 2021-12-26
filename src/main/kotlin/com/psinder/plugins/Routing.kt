package com.psinder.plugins

import com.psinder.account.configureAccountRouting
import io.ktor.application.Application

internal fun Application.configureRouting() {
    configureAccountRouting()
}
