package com.psinder.jackson

import arrow.integrations.jackson.module.registerArrowModule
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.koin.dsl.module

internal object JsonMapper {
    val defaultMapper = jacksonObjectMapper()

    init {
        defaultMapper.apply {
            registerArrowModule()
            enable(SerializationFeature.INDENT_OUTPUT)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    }
}

internal val jacksonModule = module {
    single { JsonMapper.defaultMapper }
}
