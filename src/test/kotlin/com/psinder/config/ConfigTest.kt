package com.psinder.config

import io.kotest.core.spec.style.DescribeSpec
import io.ktor.config.ApplicationConfigurationException
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isNotBlank

class ConfigTest : DescribeSpec({

    describe("config test") {

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
