package com.psinder.feature.toggle.koin

import com.psinder.feature.toggle.GetBooleanToggleQueryHandler
import org.koin.dsl.module

val featureTogglesQueryHandlersModule = module {
    single { GetBooleanToggleQueryHandler(get()) }
}
