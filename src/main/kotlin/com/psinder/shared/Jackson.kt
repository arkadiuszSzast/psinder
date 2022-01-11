package com.psinder.shared

import arrow.core.Either
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef

internal inline fun <reified T> ObjectMapper.toObject(content: String) =
    Either.catch { readValue(content, jacksonTypeRef<T>()) }

internal inline fun <reified T> ObjectMapper.toObject(content: ByteArray) =
    Either.catch { readValue(content, jacksonTypeRef<T>()) }
