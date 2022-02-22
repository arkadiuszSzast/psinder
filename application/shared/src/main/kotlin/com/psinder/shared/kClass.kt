package com.psinder.shared

import kotlin.reflect.KClass

val <T : Any> T.kClass: KClass<out T>
    get() = javaClass.kotlin

val <T : Any> T.kClassSimpleName: String
    get() = javaClass.kotlin.simpleName ?: ""
