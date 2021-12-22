package com.psinder.plugins

import com.psinder.config.TracingConfig
import com.zopa.ktor.opentracing.OpenTracingServer
import com.zopa.ktor.opentracing.ThreadContextElementScopeManager
import datadog.opentracing.DDTracer
import io.ktor.application.*
import io.opentracing.util.GlobalTracer
import datadog.trace.api.GlobalTracer as DatadogTracer

fun Application.configureOpentracing() {
    if (!TracingConfig.enabled) {
        return
    }

    val tracer = DDTracer.builder()
        .scopeManager(ThreadContextElementScopeManager())
        .build()

    GlobalTracer.registerIfAbsent(tracer)
    DatadogTracer.registerIfAbsent(tracer)

    install(OpenTracingServer)
}
