package com.psinder.shared

interface TransformableToThrowable<T: Throwable> {
    fun toThrowable(): T
}