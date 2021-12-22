package com.psinder.features

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import io.sentry.Sentry

private inline fun <T> sentryWrap(path: String, fn: () -> T) =
    try {
        fn()
    } catch (ex: Throwable) {
        Sentry.captureException(ex)
        throw ex
    } finally {
    }

class SentryFeature private constructor() {

    class Configuration {
        var dsn: String? = null
        var appEnv: String? = null
        var serverName: String? = null

        internal fun initClient() {
            Sentry.init { options ->
                options.dsn = dsn
                options.environment= appEnv
                options.serverName = serverName
                options.setDebug(true)
            }
        }
    }

    suspend fun intercept(context: PipelineContext<Unit, ApplicationCall>) {
        sentryWrap(context.context.request.path()) { context.proceed() }
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