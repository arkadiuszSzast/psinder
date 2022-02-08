package com.psinder.account

import kotlinx.serialization.Serializable

@Serializable
data class AddressData(val city: City, val streetName: StreetName? = null) {
    companion object {
        internal fun create(city: String, streetName: String?): AddressData {
            return AddressData(
                City.create(city),
                streetName?.let { StreetName.create(it) })
        }
    }
}

@JvmInline
@Serializable
value class City private constructor(val value: String) {
    companion object {
        internal fun create(city: String): City {
            return City(city.trim().replaceFirstChar { it.uppercase() })
        }
    }
}

@JvmInline
@Serializable
value class StreetName private constructor(val value: String) {
    companion object {
        internal fun create(streetName: String): StreetName {
            return StreetName(streetName.trim().replaceFirstChar { it.uppercase() })
        }
    }
}
