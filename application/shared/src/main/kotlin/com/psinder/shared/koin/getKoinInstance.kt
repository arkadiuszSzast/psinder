package com.psinder.shared.koin

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.Qualifier

inline fun <reified T> getKoinInstance(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}

inline fun <reified T> getKoinInstance(qualifier: Qualifier): T {
    return object : KoinComponent {
        val value: T by inject(qualifier)
    }.value
}
