package com.psinder.test.utils

import kotlinx.serialization.json.Json
import org.litote.kmongo.id.serialization.IdKotlinXSerializationModule

object JsonMapper {
    val defaultMapper = Json {
        useArrayPolymorphism = false
        encodeDefaults = true
        isLenient = true
        allowSpecialFloatingPointValues = true
        allowStructuredMapKeys = true
        prettyPrint = true
        ignoreUnknownKeys = true
        serializersModule = IdKotlinXSerializationModule
    }
}
