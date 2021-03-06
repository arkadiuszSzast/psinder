package com.psinder.feature.toggle

import com.configcat.ConfigCatClient
import com.trendyol.kediatr.AsyncQueryHandler
import mu.KotlinLogging

internal class GetBooleanToggleQueryHandler(private val configCatClient: ConfigCatClient) :
    AsyncQueryHandler<GetBooleanToggleQuery, GetBooleanToggleResult> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(query: GetBooleanToggleQuery): GetBooleanToggleResult {
        val allKeys = configCatClient.allKeys
        if (query.key.key !in allKeys) {
            logger.warn { "Feature toggle key not found: ${query.key.key}" }
            return GetBooleanToggleResult.NotFound(query.key)
        }

        val value = configCatClient.getValue(Boolean::class.java, query.key.key, false)
        return GetBooleanToggleResult.Found(query.key, value)
    }
}
