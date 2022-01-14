package com.psinder.account

import kotlinx.serialization.Serializable

@Serializable
internal data class PersonalData(val name: Name, val surname: Surname? = null, val addressData: AddressData? = null) {
    companion object {
        internal fun create(name: String, surname: String?, addressData: AddressData?): PersonalData {
            return PersonalData(Name.create(name), surname?.let { Surname.create(it) }, addressData)
        }
    }
}

@JvmInline
@Serializable
value class Name private constructor(val value: String) {
    companion object {
        internal fun create(name: String): Name {
            return Name(name.trim().replaceFirstChar { it.uppercase() })
        }
    }
}

@JvmInline
@Serializable
value class Surname private constructor(val value: String) {
    companion object {
        internal fun create(surname: String): Surname {
            return Surname(surname.trim().replaceFirstChar { it.uppercase() })
        }
    }
}
