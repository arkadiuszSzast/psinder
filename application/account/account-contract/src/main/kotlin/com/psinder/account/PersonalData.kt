package com.psinder.account

import kotlinx.serialization.Serializable

@Serializable
data class PersonalData(val name: Name, val surname: Surname? = null, val addressData: AddressData? = null)

@JvmInline
@Serializable
value class Name private constructor(val value: String) {
    companion object {
        fun create(name: String): Name {
            return Name(name.trim().replaceFirstChar { it.uppercase() })
        }
    }
}

@JvmInline
@Serializable
value class Surname private constructor(val value: String) {
    companion object {
        fun create(surname: String): Surname {
            return Surname(surname.trim().replaceFirstChar { it.uppercase() })
        }
    }
}
