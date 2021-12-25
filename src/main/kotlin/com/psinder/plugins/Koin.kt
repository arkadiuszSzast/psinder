package com.psinder.plugins

import com.psinder.jackson.jacksonKoinModule
import io.ktor.application.*
import org.koin.ktor.ext.Koin

internal fun Application.configureKoin() {
    install(Koin) {
        modules(jacksonKoinModule)
    }
}
