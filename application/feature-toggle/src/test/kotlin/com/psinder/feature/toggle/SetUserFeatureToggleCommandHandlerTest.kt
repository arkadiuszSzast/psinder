package com.psinder.feature.toggle

import com.psinder.auth.principal.CanDoAnythingAbilityProvider
import com.psinder.feature.toggle.client.ConfigCatSetToggleClient
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.mockk

class SetUserFeatureToggleCommandHandlerTest : DescribeSpec() {

    private val client = mockk<ConfigCatSetToggleClient>()
    private val acl = CanDoAnythingAbilityProvider()
    private val handler = SetUserFeatureToggleCommandHandler(client, acl)

    init {
    }
}
