package com.psinder.json

import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.litote.kmongo.id.serialization.IdKotlinXSerializationModule

private object JsonMapper {
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
