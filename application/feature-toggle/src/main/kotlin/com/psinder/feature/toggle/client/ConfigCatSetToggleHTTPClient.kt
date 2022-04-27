package com.psinder.feature.toggle.client

import arrow.core.nel
import com.psinder.feature.toggle.BooleanRolloutRules
import com.psinder.feature.toggle.FeatureToggleKey
import com.psinder.feature.toggle.FeatureToggleUserIdentifier
import com.psinder.feature.toggle.client.requests.PatchBooleanRolloutRulesRequest
import com.psinder.feature.toggle.client.responses.ToggleDetailsResponse
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
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.http.ContentType
import io.ktor.http.Url
import io.ktor.http.contentType
import mu.KotlinLogging

class ConfigCatSetToggleHTTPClient(private val config: ConfigCatConfig) : ConfigCatSetToggleClient {
    private val log = KotlinLogging.logger {}

    private val client by lazy {
        HttpClient(CIO) {
            install(DefaultRequest) {
                headers.appendMissing(ConfigCatApi.sdkKeyHeaderName, config.apiKey.nel())
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer(JsonMapper.defaultMapper)
            }
            install(Auth) {
                basic {
                    credentials {
                        BasicAuthCredentials(config.basicAuthUsername, config.basicAuthPassword)
                    }
                    sendWithoutRequest { true }
                }
            }
        }
    }

    override suspend fun setToggleByUserId(
        userId: FeatureToggleUserIdentifier,
        toggle: FeatureToggleKey,
        value: Boolean
    ) {
        val currentState = client.get<ToggleDetailsResponse<Boolean>>(Url(ConfigCatApi.baseTogglePath(toggle)))
        log.debug { "Going to set toggle ${toggle.key} to [$value]. Current state is ${currentState.value}" }
        val rules = BooleanRolloutRules.create(currentState.rolloutRules)
        val updatedRules = rules.enableForUser(userId)

        client.patch<ToggleDetailsResponse<Boolean>>(Url(ConfigCatApi.baseTogglePath(toggle))) {
            contentType(ContentType.Application.Json)
            body = listOf(PatchBooleanRolloutRulesRequest(updatedRules))
        }
    }
}
