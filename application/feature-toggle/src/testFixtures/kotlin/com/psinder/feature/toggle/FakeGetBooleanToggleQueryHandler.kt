package com.psinder.feature.toggle

import com.trendyol.kediatr.AsyncQueryHandler

class FakeGetBooleanToggleQueryHandler(
    private val mapper: (GetBooleanToggleQuery) -> GetBooleanToggleResult = ::defaultGetBooleanToggleQueryHandlerMapper
) : AsyncQueryHandler<GetBooleanToggleQuery, GetBooleanToggleResult> {
    override suspend fun handleAsync(query: GetBooleanToggleQuery): GetBooleanToggleResult {
        return mapper(query)
    }
}

private fun defaultGetBooleanToggleQueryHandlerMapper(query: GetBooleanToggleQuery): GetBooleanToggleResult {
    return GetBooleanToggleResult.Found(query.key, false)
}
