package com.psinder.shared.json

import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.litote.kmongo.id.serialization.IdKotlinXSerializationModule

internal object JsonMapper {
    val defaultMapper = Json {
        useArrayPolymorphism = true
        encodeDefaults = true
        isLenient = true
        allowSpecialFloatingPointValues = true
        allowStructuredMapKeys = true
        prettyPrint = true
        serializersModule = IdKotlinXSerializationModule
    }
}

internal val jacksonModule = module {
    single { JsonMapper.defaultMapper }
}
