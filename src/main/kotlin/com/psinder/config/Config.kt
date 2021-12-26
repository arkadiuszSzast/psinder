package com.psinder.config

import com.typesafe.config.ConfigFactory
import io.ktor.config.HoconApplicationConfig

internal object Config {
    internal val config = HoconApplicationConfig(ConfigFactory.load())
}

internal fun getProperty(key: ConfigKey) = Config.config.property(key.key).getString()

internal fun getPropertyAsBoolean(key: ConfigKey) = Config.config.property(key.key).getString().toBoolean()

internal fun getPropertyOrNull(key: ConfigKey) = Config.config.propertyOrNull(key.key)?.getString()

@JvmInline
internal value class ConfigKey(val key: String)
