package com.psinder.features

import io.ktor.application.ApplicationCall
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.request.path
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelineContext
import io.sentry.Sentry
import io.sentry.TransactionContext
import io.sentry.kotlin.SentryContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private inline fun <T> sentryWrap(requestPath: String, crossinline fn: () -> T) = runBlocking {
    launch(SentryContext()) {
        val transaction = Sentry.startTransaction(TransactionContext(requestPath, "http"))
        try {
            fn()
        } catch (t: Throwable) {
            Sentry.captureException(t)
            transaction.throwable = t
            throw t
        } finally {
            transaction.finish()
        }
    }
}

class SentryFeature private constructor() {

    class Configuration {
        var dsn: String? = null
        var appEnv: String? = null
        var serverName: String? = null

        internal fun initClient() {
            Sentry.init { options ->
                options.dsn = dsn
                options.environment = appEnv
                options.serverName = serverName
            }
        }
    }

    suspend fun intercept(context: PipelineContext<Unit, ApplicationCall>) {
        sentryWrap(context.context.request.path()) { runBlocking { context.proceed() } }
    }

    companion object Feature : ApplicationFeature<ApplicationCallPipeline, Configuration, SentryFeature> {
        override val key = AttributeKey<SentryFeature>("SentryFeature")
        override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): SentryFeature {
            val cfg = Configuration().apply(configure)
            cfg.initClient()
            val result = SentryFeature()

            pipeline.intercept(ApplicationCallPipeline.Call) {
                result.intercept(this)
            }
            return result
        }
    }
}
