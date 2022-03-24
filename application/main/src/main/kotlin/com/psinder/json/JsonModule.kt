package com.psinder.json

import com.psinder.shared.json.JsonMapper
import org.koin.dsl.module

val jsonModule = module {
    single { JsonMapper.defaultMapper }
}
