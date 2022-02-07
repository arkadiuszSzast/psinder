package com.psinder.shared

import org.koin.core.Koin
import org.koin.core.scope.Scope

inline fun <reified T : Any> Koin.getAllDistinct(): List<T> {
    return getAll<T>().distinct()
}

inline fun <reified T : Any> Scope.getAllDistinct(): List<T> {
    return getAll<T>().distinct()
}
