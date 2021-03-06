package com.psinder.monitoring.plugins

import com.psinder.monitoring.config.TracingConfig
import com.zopa.ktor.opentracing.OpenTracingServer
import com.zopa.ktor.opentracing.ThreadContextElementScopeManager
import com.zopa.ktor.opentracing.getGlobalTracer
import datadog.opentracing.DDTracer
import datadog.trace.api.CorrelationIdentifier
import datadog.trace.context.ScopeListener
import io.ktor.application.Application
import io.ktor.application.install
import io.opentracing.util.GlobalTracer
import org.koin.dsl.module
import org.slf4j.MDC
import datadog.trace.api.GlobalTracer as DatadogTracer

internal fun Application.configureOpentracing(tracingConfig: TracingConfig) {
    if (!tracingConfig.enabled) {
        return
    }

    val tracer = DDTracer.builder()
        .scopeManager(ThreadContextElementScopeManager())
        .build()
        .apply {
            addScopeListener(LogContextScopeListener())
        }

    GlobalTracer.registerIfAbsent(tracer)
    DatadogTracer.registerIfAbsent(tracer)

    install(OpenTracingServer)
}

internal class LogContextScopeListener : ScopeListener {
    override fun afterScopeActivated() {
        MDC.put(CorrelationIdentifier.getTraceIdKey(), CorrelationIdentifier.getTraceId())
        MDC.put(CorrelationIdentifier.getSpanIdKey(), CorrelationIdentifier.getSpanId())
    }

    override fun afterScopeClosed() {
        MDC.remove(CorrelationIdentifier.getTraceIdKey())
        MDC.remove(CorrelationIdentifier.getSpanIdKey())
    }
}

val tracerModule = module {
    single { getGlobalTracer() }
    single { DatadogTracer.get() }
}
