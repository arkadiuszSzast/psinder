package com.psinder.shared.provider

data class Provider<T>(private val getter: suspend () -> T) {
    suspend fun get(): T = getter()
}
