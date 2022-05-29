package com.psinder.shared

import io.ktor.http.ContentRange
import io.ktor.http.HttpHeaders
import io.ktor.http.RangesSpecifier
import io.ktor.request.ApplicationRequest
import io.ktor.request.header
import io.ktor.util.chomp

fun ApplicationRequest.range(): ContentRange.Bounded =
    header(HttpHeaders.Range)!!.let { rangesSpec -> parseRanges(rangesSpec)!!.ranges.single() } as ContentRange.Bounded

fun parseRanges(rangeSpec: String): RangesSpecifier? {
    try {
        val (unit, allRangesString) = rangeSpec.chomp("=") { return null }
        val allRanges = allRangesString.split(',').map {
            if (it.startsWith("-")) {
                ContentRange.Suffix(it.removePrefix("-").toLong())
            } else {
                val (from, to) = it.chomp("-") { "" to "" }
                when {
                    to.isNotEmpty() -> ContentRange.Bounded(from.toLong(), to.toLong())
                    else -> ContentRange.TailFrom(from.toLong())
                }
            }
        }

        if (allRanges.isEmpty() || unit.isEmpty()) {
            return null
        }

        val spec = RangesSpecifier(unit, allRanges)
        return if (spec.isValid { true }) spec else null
    } catch (e: Throwable) {
        return null // according to the specification we should ignore syntactically incorrect headers
    }
}
