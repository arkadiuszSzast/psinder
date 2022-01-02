package com.psinder.jackson

import arrow.integrations.jackson.module.registerArrowModule
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.koin.dsl.module
import org.litote.kmongo.id.jackson.IdJacksonModule

internal object JsonMapper {
    val defaultMapper = jacksonObjectMapper()

    init {
        defaultMapper.apply {
            registerArrowModule()
            registerModule(IdJacksonModule())
            registerModule(JavaTimeModule())
            enable(SerializationFeature.INDENT_OUTPUT)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    }
}

internal val jacksonModule = module {
    single { JsonMapper.defaultMapper }
}
