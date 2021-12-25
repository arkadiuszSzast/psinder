package com.psinder.monitoring

import io.opentracing.Span
import io.opentracing.Tracer

inline fun <T> Span.activate(tracer: Tracer, block: () -> T): T {
    try {
        tracer.activateSpan(this).use {
            return block()
        }
    } catch (ex: Throwable) {
        setErrorAndLog(ex)
        throw ex
    } finally {
        finish()
    }
}
