package com.psinder.feature.toggle

import com.configcat.ConfigCatClient
import com.trendyol.kediatr.AsyncQueryHandler
import mu.KotlinLogging

internal class GetBooleanToggleQueryHandler(private val configCatClient: ConfigCatClient) :
    AsyncQueryHandler<GetBooleanToggleQuery, GetBooleanToggleResult> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(query: GetBooleanToggleQuery): GetBooleanToggleResult {
        val value = configCatClient.getValue(Boolean::class.java, query.key.key, false)
        return GetBooleanToggleResult.Found(query.key, value)
    }
}
