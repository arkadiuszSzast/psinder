package com.psinder.config

import io.kotest.core.spec.style.DescribeSpec
import io.ktor.config.ApplicationConfigurationException
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isNotBlank
import strikt.assertions.isNotEqualTo

class ConfigTest : DescribeSpec({

    describe("config test") {

        it("load config from file") {
            val config = Config.config

            expectThat(config)
                .isNotEqualTo(null)
        }

        it("get property") {
            expectThat(getProperty(ConfigKey("jwt.domain")))
                .isNotBlank()
        }

        it("throw exception when property does not exist") {
            expectThrows<ApplicationConfigurationException> {
                getProperty(ConfigKey("not_existing"))
            }
        }
    }
})
