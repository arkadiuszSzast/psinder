package com.psinder.test.utils

import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

suspend fun <R> withKoin(module: Module, action: suspend Koin.() -> R) {
    try {
        val koin = startKoin {
            modules(module)
        }.koin
        action(koin)
    } finally {
        stopKoin()
    }
}
