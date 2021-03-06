package com.psinder.shared.json

import arrow.core.Either
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

inline fun <reified T> Json.decodeFromString(content: String) =
    Either.catch { decodeFromString<T>(content) }

inline fun <reified T> Json.decodeFromStream(content: ByteArray) =
    Either.catch { decodeFromStream<T>(content.inputStream()) }
