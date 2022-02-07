package com.psinder.monitoring

import io.opentracing.Span
import io.opentracing.log.Fields
import io.opentracing.tag.Tags

fun Span.setErrorAndLog(ex: Throwable) {
    setTag(Tags.ERROR, true)
    log(
        mapOf(
            Fields.EVENT to Tags.ERROR.key,
            Fields.ERROR_KIND to ex.javaClass.name,
            Fields.MESSAGE to ex.message,
            Fields.ERROR_OBJECT to ex,
            Fields.STACK to ex.stackTraceToString()
        )
    )
}
