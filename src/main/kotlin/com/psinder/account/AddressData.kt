package com.psinder.account

import com.fasterxml.jackson.annotation.JsonCreator

internal data class AddressData(val city: City, val streetName: StreetName? = null) {
    companion object {
        @JvmStatic
        @JsonCreator
        internal fun create(city: String, streetName: String?): AddressData {
            return AddressData(City.create(city), streetName?.let { StreetName.create(it) })
        }
    }
}

@JvmInline
internal value class City private constructor(val value: String) {
    companion object {
        internal fun create(city: String): City {
            return City(city.trim().replaceFirstChar { it.uppercase() })
        }
    }
}

@JvmInline
internal value class StreetName private constructor(val value: String) {
    companion object {
        internal fun create(streetName: String): StreetName {
            return StreetName(streetName.trim().replaceFirstChar { it.uppercase() })
        }
    }
}
