package com.psinder.plugins

import com.psinder.account.configureAccountRouting
import io.ktor.application.*

internal fun Application.configureRouting() {
    configureAccountRouting()
}
