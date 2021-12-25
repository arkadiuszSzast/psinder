package com.psinder.shared

internal val Throwable.rootCause: Throwable
    get() {
        var rootCause: Throwable? = this
        while (rootCause?.cause != null) {
            rootCause = rootCause.cause
        }
        return rootCause ?: this
    }

