package com.psinder.feature.toggle.config

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class ConfigCatConfigTest : DescribeSpec() {

    init {
        describe("get config cat config") {

            it("get properties") {
                // arrange && act && assert
                expectThat(ConfigCatConfig) {
                    get { apiKey }.isEqualTo("test_config_cat_secret")
                    get { basicAuthUsername }.isEqualTo("test_config_cat_username")
                    get { basicAuthPassword }.isEqualTo("test_config_cat_password")
                }
            }
        }
    }
}
