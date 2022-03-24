package com.psinder.test.utils

import com.psinder.test.utils.JsonMapper.defaultMapper
import org.koin.dsl.module

val serializationModule = module {
    single { defaultMapper }
}
