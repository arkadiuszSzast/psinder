package com.psinder.config

import com.typesafe.config.ConfigFactory
import io.ktor.config.HoconApplicationConfig

private object ConfigMap {
    val config = HoconApplicationConfig(ConfigFactory.load())
}

internal fun getProperty(key: ConfigKey) = ConfigMap.config.property(key.key).getString()

internal fun getPropertyAsBoolean(key: ConfigKey) = ConfigMap.config.property(key.key).getString().toBoolean()

internal fun getPropertyOrNull(key: ConfigKey) = ConfigMap.config.propertyOrNull(key.key)?.getString()

@JvmInline
internal value class ConfigKey(val key: String)
