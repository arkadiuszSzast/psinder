package com.psinder.feature.toggle.koin

import com.psinder.feature.toggle.GetBooleanToggleQueryHandler
import com.psinder.feature.toggle.SetUserFeatureToggleCommandHandler
import com.psinder.feature.toggle.client.ConfigCatSetToggleClient
import org.koin.dsl.module

val featureTogglesCommandHandlersModule = module {
    single { SetUserFeatureToggleCommandHandler(ConfigCatSetToggleClient, get()) }
}
