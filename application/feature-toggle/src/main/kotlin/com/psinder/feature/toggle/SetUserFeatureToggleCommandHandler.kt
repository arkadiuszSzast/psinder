package com.psinder.feature.toggle

import com.psinder.auth.authority.setUserFeatureToggleFeature
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.feature.toggle.client.ConfigCatSetToggleClient
import com.psinder.feature.toggle.client.requests.PatchBooleanRolloutRulesRequest
import com.psinder.feature.toggle.client.responses.ToggleDetailsResponse
import com.psinder.feature.toggle.config.ConfigCatApi
import com.trendyol.kediatr.AsyncCommandHandler
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.http.ContentType
import io.ktor.http.Url
import io.ktor.http.contentType
import mu.KotlinLogging

internal class SetUserFeatureToggleCommandHandler(
    private val configCatSetToggleClient: ConfigCatSetToggleClient,
    private val acl: AuthorizedAccountAbilityProvider
) : AsyncCommandHandler<SetUserFeatureToggleCommand> {
    private val log = KotlinLogging.logger {}

    override suspend fun handleAsync(command: SetUserFeatureToggleCommand) {
        acl.ensure().hasAccessTo(setUserFeatureToggleFeature)
        val client = configCatSetToggleClient.client
        val (userId, toggle, value) = command

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
