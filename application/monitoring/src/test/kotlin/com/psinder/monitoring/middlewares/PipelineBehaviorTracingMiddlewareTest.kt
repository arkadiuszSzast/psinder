package com.psinder.monitoring.middlewares

import com.psinder.test.utils.withLogRecorder
import com.trendyol.kediatr.Command
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.opentracing.mock.MockTracer
import mu.KotlinLogging
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.containsIgnoringCase
import strikt.assertions.containsKey
import strikt.assertions.elementAt
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import strikt.assertions.map
import strikt.assertions.withFirst

class PipelineBehaviorTracingMiddlewareTest : DescribeSpec() {
    override fun isolationMode() = IsolationMode.InstancePerTest

    private val tracer = MockTracer()
    private val logger = KotlinLogging.logger {}

    init {

        describe("PipelineBehaviorTracingMiddleware") {

            it("should start span") {
                withLogRecorder {
                    val logAppender = this
                    val middleware = PipelineBehaviorTracingMiddleware(tracer)
                    middleware.apply(HelloCommand()) {
                        logger.info { "Hello!" }
                    }

                    expectThat(tracer) {
                        get { finishedSpans() }
                            .hasSize(1)
                            .withFirst {
                                get { operationName() }
                                    .isEqualTo("command-handler-${HelloCommand::class.simpleName}")
                                get { tags() }
                                    .hasSize(1)
                                    .containsKey("command")
                                    .get { this["command"] }.isEqualTo(HelloCommand::class.simpleName)
                            }
                    }

                    expectThat(logAppender) {
                        get { events }
                            .hasSize(3)
                            .map { it.message }
                            .and { elementAt(0).containsIgnoringCase("executing command ${HelloCommand::class.simpleName}") }
                            .and { elementAt(1).containsIgnoringCase("Hello!") }
                            .and { elementAt(2).containsIgnoringCase("executed successfully") }
                    }
                }
            }

            it("should log error") {
                withLogRecorder {
                    val logAppender = this
                    val middleware = PipelineBehaviorTracingMiddleware(tracer)

                    expectThrows<Error> {
                        middleware.apply(HelloCommand()) {
                            logger.info { "Hello before error!" }
                            throw Error()
                        }
                    }

                    expectThat(tracer) {
                        get { finishedSpans() }
                            .hasSize(1)
                            .withFirst {
                                get { operationName() }
                                    .isEqualTo("command-handler-${HelloCommand::class.simpleName}")
                                get { tags() }
                                    .hasSize(2)
                                    .containsKey("command")
                                    .containsKey("error")
                                    .and { get { this["command"] }.isEqualTo(HelloCommand::class.simpleName) }
                                    .and { get { this["error"] }.isEqualTo(true) }
                            }
                    }

                    expectThat(logAppender) {
                        get { events }
                            .hasSize(3)
                            .map { it.message }
                            .and { elementAt(0).containsIgnoringCase("executing command ${HelloCommand::class.simpleName}") }
                            .and { elementAt(1).containsIgnoringCase("Hello before error!") }
                            .and { elementAt(2).containsIgnoringCase("Error while executing command") }
                    }
                }
            }
        }
    }
}

private class HelloCommand : Command
