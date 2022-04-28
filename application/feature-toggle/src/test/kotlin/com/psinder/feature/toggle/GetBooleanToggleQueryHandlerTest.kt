package com.psinder.feature.toggle

import com.configcat.ConfigCatClient
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class GetBooleanToggleQueryHandlerTest : DescribeSpec() {

    private val configCatClient = mockk<ConfigCatClient>()
    private val handler = GetBooleanToggleQueryHandler(configCatClient)

    init {
        describe("GetBooleanToggleQueryHandler") {

            it("should return true when toggle is enabled") {
                //arrange
                val featureToggleKey = FeatureToggleKey("feature_a")
                val query = GetBooleanToggleQuery(featureToggleKey)

                every { configCatClient.getValue(Boolean::class.java, featureToggleKey.key, false) } returns true
                every { configCatClient.allKeys } returns listOf(featureToggleKey.key)

                //act
                val result = handler.handleAsync(query)

                //assert
                expectThat(result)
                    .isA<GetBooleanToggleResult.Found>()
                    .and { get { key }.isEqualTo(featureToggleKey) }
                    .and { get { value }.isEqualTo(true) }
            }

            it("should return false when toggle is disabled") {
                //arrange
                val featureToggleKey = FeatureToggleKey("feature_a")
                val query = GetBooleanToggleQuery(featureToggleKey)

                every { configCatClient.getValue(Boolean::class.java, featureToggleKey.key, false) } returns false
                every { configCatClient.allKeys } returns listOf(featureToggleKey.key)

                //act
                val result = handler.handleAsync(query)

                //assert
                expectThat(result)
                    .isA<GetBooleanToggleResult.Found>()
                    .and { get { key }.isEqualTo(featureToggleKey) }
                    .and { get { value }.isEqualTo(false) }
            }

            it("should return not found when toggle does not exist") {
                //arrange
                val featureToggleKey = FeatureToggleKey("not_existing_toggle")
                val query = GetBooleanToggleQuery(featureToggleKey)

                every { configCatClient.getValue(Boolean::class.java, featureToggleKey.key, false) } returns false
                every { configCatClient.allKeys } returns listOf("feature_a")

                //act
                val result = handler.handleAsync(query)

                //assert
                expectThat(result)
                    .isA<GetBooleanToggleResult.NotFound>()
                    .and { get { key }.isEqualTo(featureToggleKey) }
            }
        }
    }
}
