package com.psinder.feature.toggle.client

import arrow.core.nel
import com.psinder.feature.toggle.FeatureToggleKey
import com.psinder.feature.toggle.FeatureToggleUserIdentifier
import com.psinder.feature.toggle.config.ConfigCatApi
import com.psinder.feature.toggle.config.ConfigCatConfig
import com.psinder.shared.json.JsonMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.DefaultRequest
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.BasicAuthCredentials
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer

object ConfigCatSetToggleClient {
    val client by lazy {
        HttpClient(CIO) {
            install(DefaultRequest) {
                headers.appendMissing(ConfigCatApi.sdkKeyHeaderName, ConfigCatConfig.apiKey.nel())
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer(JsonMapper.defaultMapper)
            }
            install(Auth) {
                basic {
                    credentials {
                        BasicAuthCredentials(ConfigCatConfig.basicAuthUsername, ConfigCatConfig.basicAuthPassword)
                    }
                    sendWithoutRequest { true }
                }
            }
        }
    }
}
