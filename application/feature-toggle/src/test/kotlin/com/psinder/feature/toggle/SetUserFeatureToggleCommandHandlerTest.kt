package com.psinder.feature.toggle

import arrow.core.nel
import com.psinder.auth.AuthorityCheckException
import com.psinder.auth.principal.CanDoAnythingAbilityProvider
import com.psinder.auth.principal.DenyAllAbilityProvider
import com.psinder.feature.toggle.client.ConfigCatSetToggleClient
import com.psinder.feature.toggle.client.requests.PatchBooleanRolloutRulesRequest
import com.psinder.feature.toggle.client.responses.ToggleDetailsResponse
import com.psinder.shared.json.decodeFromStream
import com.psinder.test.utils.JsonMapper
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.encodeToString
import pl.brightinventions.codified.enums.codifiedEnum
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.arrow.isRight
import strikt.assertions.first
import strikt.assertions.hasSize
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isFailure

class SetUserFeatureToggleCommandHandlerTest : DescribeSpec() {

    private val client = mockk<ConfigCatSetToggleClient>()
    private val allowAllAcl = CanDoAnythingAbilityProvider()
    private val denyAllAcl = DenyAllAbilityProvider()
    private val allowingHandler = SetUserFeatureToggleCommandHandler(client, allowAllAcl)
    private val denyingHandler = SetUserFeatureToggleCommandHandler(client, denyAllAcl)
    private val json = JsonMapper.defaultMapper

    init {

        describe("SetUserFeatureToggleCommandHandler") {

            it("should enable for user") {
                // arrange
                val userId = FeatureToggleUserIdentifier("user_1")
                val rulesAfterPatch = BooleanRolloutRules.create(emptyList()).enableForUser(userId)
                val mockEngine = MockEngine { request ->
                    when (request.method) {
                        HttpMethod.Get -> respond(
                            json.encodeToString(
                                ToggleDetailsResponse<Boolean>(
                                    toggleASettings(), null, "true", emptyList()
                                )
                            ).encodeToByteArray(),
                            headers = headersOf(HttpHeaders.ContentType, "application/json")
                        )
                        HttpMethod.Patch -> respond(
                            json.encodeToString(
                                ToggleDetailsResponse(
                                    toggleASettings(), null, "true", rulesAfterPatch
                                )
                            ).encodeToByteArray(),
                            headers = headersOf(HttpHeaders.ContentType, "application/json")
                        )

                        else -> respond("Error", HttpStatusCode.InternalServerError)
                    }
                }
                val mockClient = HttpClient(mockEngine) {
                    install(JsonFeature) {
                        serializer = KotlinxSerializer(json)
                    }
                }
                every { client.client } returns mockClient

                // act
                allowingHandler.handleAsync(
                    SetUserFeatureToggleCommand(
                        FeatureToggleUserIdentifier("user_1"), FeatureToggleKey("toggle_a"), true
                    )
                )

                // assert
                expectThat(mockEngine.requestHistory) {
                    and { hasSize(2) }
                    and {
                        first { it.method == HttpMethod.Patch }
                            .get { runBlocking { body.toByteArray() } }
                            .get { json.decodeFromStream<List<PatchBooleanRolloutRulesRequest>>(this) }
                            .isRight()
                            .get { value }
                            .isEqualTo(PatchBooleanRolloutRulesRequest(rulesAfterPatch).nel())
                    }
                }
            }

            it("should throw exception when insufficient permissions") {
                // arrange
                val userId = FeatureToggleUserIdentifier("user_1")
                val rulesAfterPatch = BooleanRolloutRules.create(emptyList()).enableForUser(userId)
                val mockEngine = MockEngine { request ->
                    when (request.method) {
                        HttpMethod.Get -> respond(
                            json.encodeToString(
                                ToggleDetailsResponse<Boolean>(
                                    toggleASettings(), null, "true", emptyList()
                                )
                            ).encodeToByteArray(),
                            headers = headersOf(HttpHeaders.ContentType, "application/json")
                        )
                        HttpMethod.Patch -> respond(
                            json.encodeToString(
                                ToggleDetailsResponse(
                                    toggleASettings(), null, "true", rulesAfterPatch
                                )
                            ).encodeToByteArray(),
                            headers = headersOf(HttpHeaders.ContentType, "application/json")
                        )

                        else -> respond("Error", HttpStatusCode.InternalServerError)
                    }
                }
                val mockClient = HttpClient(mockEngine) {
                    install(JsonFeature) {
                        serializer = KotlinxSerializer(json)
                    }
                }
                every { client.client } returns mockClient

                // act
                expectCatching {
                    denyingHandler.handleAsync(
                        SetUserFeatureToggleCommand(
                            FeatureToggleUserIdentifier("user_1"), FeatureToggleKey("toggle_a"), true
                        )
                    )
                }.isFailure().isA<AuthorityCheckException>()
            }
        }
    }

    private fun toggleASettings() =
        ToggleSetting(1, "toggle_a", null, null, SettingType.Boolean.codifiedEnum(), 1, null)
}
