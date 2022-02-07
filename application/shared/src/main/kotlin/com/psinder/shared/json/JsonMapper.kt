package com.psinder.shared.json

import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.litote.kmongo.id.serialization.IdKotlinXSerializationModule

object JsonMapper {
    val defaultMapper = Json {
        useArrayPolymorphism = true
        encodeDefaults = true
        isLenient = true
        allowSpecialFloatingPointValues = true
        allowStructuredMapKeys = true
        prettyPrint = true
        ignoreUnknownKeys = true
        serializersModule = IdKotlinXSerializationModule
    }
}

val jsonModule = module {
    single { JsonMapper.defaultMapper }
}
