package com.psinder.test.utils

import com.psinder.shared.json.decodeFromString
import io.ktor.server.testing.TestApplicationResponse
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import strikt.api.Assertion
import java.time.LocalDateTime.now
import java.time.ZoneId

inline fun <reified T> Assertion.Builder<TestApplicationResponse>.contentIsTypeOf() {
    assert("Request content can be deserialized to: ${T::class.qualifiedName}") {
        with(it.content) {
            when (this) {
                null -> fail("Cannot construct [${T::class.qualifiedName}] from null.")
                else -> {
                    val result = JsonMapper.defaultMapper.decodeFromString<T>(this)
                    result.fold(
                        { fail("Cannot construct [${T::class.qualifiedName}] from given string: $this. Error: ${it.message}") },
                        { pass() }
                    )
                }
            }
        }
    }
}

fun Assertion.Builder<LocalDateTime>.isEqualToNowIgnoringSeconds() {
    assert("LocalDateTime is equal to now ignoring seconds") {
        val nowMinusOneSecond = now(ZoneId.of("UTC")).minusSeconds(1)
        val nowPlusOneSecond = now(ZoneId.of("UTC")).plusSeconds(1)
        val givenDate = it.toJavaLocalDateTime()
        if (givenDate.isBefore(nowPlusOneSecond) && givenDate.isAfter(nowMinusOneSecond)) {
            pass()
        } else {
            fail("Given LocalDateTime [$it] is not between $nowMinusOneSecond and $nowPlusOneSecond")
        }
    }
}

fun Assertion.Builder<Instant>.isUpToOneSecondOld() {
    assert("Instant is equal to now ignoring seconds") {
        val epochSecondsNow = Clock.System.now().epochSeconds
        val epochSecondsMinusOne = epochSecondsNow - 1
        val givenEpochSeconds = it.epochSeconds

        if (givenEpochSeconds >= epochSecondsMinusOne || givenEpochSeconds <= epochSecondsNow) {
            pass()
        } else {
            fail("Given Instant is equal is not between $epochSecondsMinusOne and $epochSecondsNow")
        }
    }
}
