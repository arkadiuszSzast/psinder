package com.psinder.test.utils

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.Logger as LogbackLogger

class TestLogAppender : AppenderBase<ILoggingEvent>() {
    private val _events: MutableList<ILoggingEvent> = mutableListOf()

    val events by lazy { _events.toList() }

    override fun append(eventObject: ILoggingEvent) {
        _events.add(eventObject)
    }
}

fun <R> withLogRecorder(action: TestLogAppender.() -> R) {
    val logAppender = TestLogAppender()
    (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as LogbackLogger).apply {
        addAppender(logAppender)
    }
    logAppender.start()

    try {
        action(logAppender)
    } finally {
        logAppender.stop()
    }
}
